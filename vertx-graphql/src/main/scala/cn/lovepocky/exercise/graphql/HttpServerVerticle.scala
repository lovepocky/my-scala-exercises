package cn.lovepocky.exercise.graphql

import com.typesafe.scalalogging.LazyLogging
import com.xyz10.base.log.ExecuteContext.CustomMdcExecuteContext
import io.vertx.core.{AbstractVerticle, Context, Vertx}
import io.vertx.lang.scala._
import io.vertx.scala.core.http.HttpServer
import io.vertx.scala.ext.web.Router
import io.vertx.scala.ext.web.handler.BodyHandler
import org.joda.time.DateTime
import org.slf4j.MDC

import scala.async.Async.async
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

class HttpServerVerticle extends ScalaVerticle with LazyLogging {

  lazy val server: HttpServer = vertx.createHttpServer()
  lazy val router: Router     = Router.router(vertx)
  val port                    = 8000

  override def init(vertx: Vertx, context: Context, verticle: AbstractVerticle): Unit = {
    super.init(vertx, context, verticle)
//    MDC.put("verticle_id", this.hashCode().toString)
    initServer()
  }

  def initServer(): Unit = {

    router.route.handler(BodyHandler.create())

    router
      .route()
      .handler { ctx =>
        logger.debug(s"executionContext.hashCode() = ${executionContext.hashCode()}")
        ctx.put("request_id", java.util.UUID.randomUUID().toString)
        ctx.next()
      }
      .handler { ctx =>
        val rid                           = ctx.get[String]("request_id")
        val ec_ctx                        = VertxExecutionContext(ctx.vertx().getOrCreateContext())
        implicit val ec: ExecutionContext = CustomMdcExecuteContext.fromThread(Map("request_id" -> rid), ec_ctx)
        async {
          logger.debug(s"request ${ctx.normalisedPath()} start")
          logger.debug(s"request_id = $rid")
          ctx.put("start_time", DateTime.now())
          ctx.next()
        }(ec)
      }

    router
      .route("/test")
      .handler { ctx =>
        ctx.response().end()
        ctx.next()
      }

    router
      .route("/")
      .handler { ctx =>
        //handler 0
        //          async {
        //          Thread.sleep(2000)
        val response = ctx.response()

        response.putHeader("content-type", "text/plain")
        // Write to the response and end it
        //          response.end("Hello World from Vert.x-Web!")
        logger.info("request_id in event loop")

        /**
          * NOTE: [important]
          * 这里运行的next, 导致下一个handler的运行, 还是在当前的context
          * 因此 handler 1, 2 都是带了MDC
          * 因此这里加一个中间件可以在handler都是event-loop的情况下使用相同的context
          * 如果中间有blocking的, 那么就要手动处理
          */
        //            ctx.next()
        //          }

        /**
          * 这里运行next, 导致使用当前的context, 没有MDC
          */
        ctx.next()
      }
      .handler { ctx =>
        //handler 1
        logger.info("assert with request_id 1")
        ctx.next()
      }
      .handler { ctx =>
        //handler 2
        logger.info("assert with request_id 2")
        ctx.next()
      }
      .blockingHandler { ctx =>
        //handler 3
        /**
          * 使用blocking, 会截断context
          * 要完全实现自动的MDC, 那么需要一个context同时管理event-loop和worker
          * 而且要注入到vertx中
          */
        logger.info("in blockingHandler 1")
        Thread.sleep(2000)
        ctx.response.end("Hello World from Vert.x-Web!")
        ctx.next()
      }
      .handler { ctx =>
        //handler 4
        logger.info("assert without request_id 3")
        ctx.next()
      }
      .blockingHandler { ctx =>
        /**
          * TODO https://zhuanlan.zhihu.com/p/34175383
          * 写一个traceable的函数
          */
        //handler 5
        val rid                           = ctx.get[String]("request_id")
        val ec_ctx                        = VertxExecutionContext(ctx.vertx().getOrCreateContext())
        implicit val ec: ExecutionContext = new CustomMdcExecuteContext(Map("request_id" -> rid), ec_ctx)
        logger.info("in blockingHandler 2")
        async {
          logger.info("another request_id in event loop")
          ctx.next()
        }(ec)
      }

    router.post("/graphql").handler { ctx =>
      import SangriaExample._

      import sangria.parser.QueryParser
      import sangria.ast._
      import sangria.marshalling.circe.CirceInputUnmarshaller
      import io.circe.{Json => CJson}

      import rapture.json.{Json => RJson, _}
      import rapture.data._
      import formatters.compact._
      import rapture.json.jsonBackends.vertx._

      ctx.getBodyAsJson() match {
        case Some(body) =>
          //            println(s"body = ${body}")
          val all           = fromJson(body)
          val query         = (all \ "query").as[String]
          val operationName = (all \ "operationName").as[Option[String]]
          val variables     = CJson.obj() // RJson.format(all \ "variables")

          QueryParser.parse(query) match {
            case Success(queryAst) =>
              //                println(s"query = ${queryAst}")
              val f   = executeGraphQLQuery(queryAst, operationName, variables)
              val res = Await.result(f, Duration.Inf)
              //                println(s"res = $res")
              ctx.response().end(res.encode())

            case Failure(f) =>
              ctx.response().end("failed")
          }
        case None =>
          ctx.response().end("need post body")
      }
      ctx.next()

      import io.vertx.lang.scala.json.JsonObject
      def executeGraphQLQuery(query: Document, op: Option[String], vars: CJson): scala.concurrent.Future[JsonObject] = {
        import sangria.execution._
        import sangria.marshalling.vertx.VertxResultMarshaller
        import io.vertx.lang.scala.json.{Json => VJson}
        //TODO replace ExecutionContext
        import scala.concurrent.ExecutionContext.Implicits.global
        Executor
          .execute(schema, query, new ProductRepo, operationName = op, variables = vars)
          .map(_.asInstanceOf[JsonObject])
          .recover {
            case error: QueryAnalysisError =>
              println(error)
              VJson.obj("message" -> error.getMessage)
            case error: ErrorWithResolver =>
              println(error)
              VJson.obj("message" -> error.getMessage)
          }
      }
    }

    router.route().handler { ctx =>
      val spend      = new org.joda.time.Duration(ctx.get[DateTime]("start_time"), DateTime.now()).getMillis
      val request_id = ctx.get[String]("request_id")
      org.slf4j.MDC.put("request_id", request_id)
      logger.debug(s"request ${ctx.normalisedPath()} over, spend time = $spend ms")
      org.slf4j.MDC.remove("request_id")
    }
  }

  override def start(): Unit = {
    logger.debug(s"start bind port to $port")
    server
      .requestHandler(router.accept _)
      .listen(port)
    logger.info(s"http server listen at $port complete")
  }

  override def stop(): Unit = {
    logger.info(s"http server listen at $port unbinding")
    server.close()
  }

}
