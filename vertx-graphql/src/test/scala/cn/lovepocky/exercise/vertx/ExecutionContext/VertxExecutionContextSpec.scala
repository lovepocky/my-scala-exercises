package cn.lovepocky.exercise.vertx.ExecutionContext

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.WordSpec
import org.slf4j.MDC

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class VertxExecutionContextSpec extends WordSpec with LazyLogging {

  import scala.concurrent.ExecutionContext
  import io.vertx.lang.scala.VertxExecutionContext
  import io.vertx.scala.core.{Future => VFuture, _}
  import scala.concurrent.Future
  import scala.async.Async._
  import com.xyz10.base.log.ExecuteContext.CustomMdcExecuteContext

  "vertx execution context" should {
    lazy val vertx = Vertx.vertx()

    "inject mdc value" in {
      MDC.put("key", "1")
      val ctx                           = vertx.getOrCreateContext()
      implicit val ec: ExecutionContext = CustomMdcExecuteContext.fromThread(VertxExecutionContext(ctx))

      logger.info("from main")

      val f = async {
        logger.info("from async")
      }
      Await.result(f, Duration.Inf)

    }

  }

}
