package cn.lovepocky.exercise.json.vertx

import io.vertx.core.buffer.Buffer
import org.scalatest.WordSpec

import scala.concurrent.ExecutionContext
import scala.util.Try

class VertxJsonSpec extends WordSpec {

  "vertx json" should {
    import io.vertx.lang.scala.json.{Json => SJson}
    import io.vertx.core.json.{Json => CJson}

    "create vertx json" in {
      val res = SJson.arr(
        SJson.obj("a" -> "b")
      )
      println(res)
    }

    "parse string" in {
      val res =
        SJson.fromArrayString("""
          |[1,2,3]
        """.stripMargin)
      println(res)
      SJson.fromArrayString("""
                             |{
                             | "name": "abc"
                             |}
                           """.stripMargin)
    }

    "parse obj" in {
      val res =
        SJson.fromObjectString("""
            |{
            | "name": "abc"
            |}
          """.stripMargin)

      println(res)
    }

    "parse arbitrary json string" in {
      import io.vertx.core.parsetools.JsonEventType._
      import io.vertx.scala.core.parsetools.JsonParser

      implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
      lazy val parser: JsonParser       = JsonParser.newParser()
      val j_string                      = """
                       |{
                       | "name": "abc"
                       |}
                     """.stripMargin

//      parser.write(Buffer.buffer(j_string)).handler { event =>
//        case
//        }

    }

  }

}
