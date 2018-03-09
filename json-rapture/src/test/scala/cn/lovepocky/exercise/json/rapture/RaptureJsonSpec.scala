package cn.lovepocky.exercise.json.rapture

import org.scalatest.FlatSpec

class RaptureJsonSpec extends FlatSpec {

  "rapture json" should "work" in {}

  it should "use in vertx" in {
    import io.vertx.core.json.{JsonObject => VJsonObject, Json => VJson}
    import io.vertx.lang.scala.json.{Json => VSJson}
    import rapture.json.jsonBackends.jackson._
    import rapture.json._

    /*io.vertx.core.json.Json.mapper.registerModule(
      com.fasterxml.jackson.module.scala.DefaultScalaModule
    )*/
    val vsjson = VSJson.obj("name" -> "abc", "age" -> 18)

    val j: Json = json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    val j2      = json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    println(j)

    val j3 = Json.construct(j.$root, Vector())

    val v = j.$root.value
    println("over")
  }

  it should "use different backend" in {
    import rapture.json._
    val j_jackson = {
      import rapture.json.jsonBackends.jackson._
      import rapture.json._

      json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    }

    val j_circe = {
      import rapture.json.jsonBackends.circe._
      json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    }

    println("over")
  }

}
