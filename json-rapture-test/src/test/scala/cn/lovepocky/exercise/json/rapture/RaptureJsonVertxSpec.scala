package cn.lovepocky.exercise.json.rapture

import org.scalatest.WordSpec

class RaptureJsonVertxSpec extends WordSpec {

  "rapture json backend vertx" should {
    import rapture.json.{Json => RJson, _}
    import rapture.json.jsonBackends.vertx._

    import io.vertx.lang.scala.json.{JsonArray, JsonObject, Json => VJson}

    "create rapture json" in {
      val rjson = json"""
      {
        "name": "hello"
      }"""
      println(rjson)
    }

    "create rjson from vjson" in {
      val vjson = VJson.obj(
        "name" -> "hello"
      )
      println(fromJson(vjson))
    }

    "unwarp rjson to vjson" in {
      val rjson = json"""
      {
        "name": "hello"
      }"""
      println(rjson.$normalize)
      println((rjson \ "name").$normalize)
      assert(rjson.$normalize.isInstanceOf[JsonObject])
      assert((rjson \ "name").$normalize.isInstanceOf[String])
    }

  }

}
