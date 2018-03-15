package cn.lovepocky.exercise.vertx.WebClient

import org.scalatest.FlatSpec

import scala.concurrent.Await
import scala.concurrent.duration.Duration

class VertxWebClientSpec extends FlatSpec {

  import io.vertx.scala.core._
  import wvlet.airframe._
  import scala.async.Async._
  import io.vertx.scala.ext.web.client.WebClient

  /*lazy val design = newDesign
    .bind[Vertx]
    .toInstance(Vertx.vertx())*/

  lazy val vertx: Vertx = Vertx.vertx()

  "vertx web client" should "work" in {
    import concurrent.ExecutionContext.Implicits.global
    import scala.async.Async._

    val f = async {
      val client = WebClient.create(vertx)

      {
        val res = await(client.get(port = 443, host = "www.taobao.com", "/").ssl(true).sendFuture())
//        println(res.body())
        println(res.statusCode())
        assert(res.statusCode() == 200)
      } {
        val res = await(client.get(host = "www.taobao.com", "/").followRedirects(false).sendFuture())
//        println(res.body())
        println(res.statusCode())
        assert(res.statusCode() == 302)
      }
    }
    Await.result(f, Duration.Inf)
  }

}
