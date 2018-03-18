package cn.lovepocky.exercise.graphql

import com.typesafe.scalalogging.LazyLogging
import com.xyz10.base.log.ExecuteContext.CustomMdcExecuteContext
import wvlet.airframe._
import io.vertx.lang.scala._
import io.vertx.scala.core._
import io.vertx.scala.core.dns.AddressResolverOptions
import io.vertx.scala.core.http.HttpServerOptions
import io.vertx.scala.ext.web._
import io.vertx.scala.ext.web.handler.BodyHandler
import org.joda.time.DateTime

import scala.concurrent.{Await, ExecutionContext}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}
import scala.async.Async._

object VertxGraphqlServer {

  def main(args: Array[String]): Unit = {
    val d = newDesign
      .bind[Vertx]
      .toInstance(
        Vertx.vertx()
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
      vertx.deployVerticle(
        ScalaVerticle.nameForVerticle[HttpServerVerticle],
        DeploymentOptions().setInstances(1)
      )
    }
  }

}
