package cn.lovepocky.exercise.graphql

import com.typesafe.scalalogging.LazyLogging
import wvlet.airframe._
import io.vertx.lang.scala._
import io.vertx.scala.core._
import io.vertx.scala.core.dns.AddressResolverOptions
import io.vertx.scala.ext.web._
import io.vertx.scala.ext.web.handler.BodyHandler
import org.joda.time.DateTime

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.tools.nsc.doc.html.page.JSONObject
import scala.util.{Failure, Success}

object VertxGraphqlServer {

  def main(args: Array[String]): Unit = {
    val d = newDesign
      .bind[Vertx]
      .toInstance(
        Vertx.vertx(VertxOptions()
            .setAddressResolverOptions(
              AddressResolverOptions().setServers(Set("192.168.1.1").toBuffer)
            ))
      )

    d.withSession { session =>
      val app = session.build[App]
      app.start(args)
      println("started")
      while (true) {
        Thread.sleep(1000)
      }
    }

  }

  class App extends LazyLogging {

    lazy val vertx: Vertx = bind[Vertx].beforeShutdown { v =>
      logger.info("closing vertx")
      v.close()
    }

    def start(args: Array[String]): Unit = {
      logger.info("app start")
      val server = vertx.createHttpServer()

      val router = Router.router(vertx)

      router.route.handler(BodyHandler.create())

      router.route("/").handler { routingContext =>
        val response = routingContext.response()
        response.putHeader("content-type", "text/plain")
        // Write to the response and end it
        response.end("Hello World from Vert.x-Web!")
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

      val port = 8000
      logger.debug(s"start bind port to $port")
      server
        .requestHandler(router.accept _)
        .listen(port, "0.0.0.0")
      logger.info(s"listen at $port complete")
    }
  }

}
