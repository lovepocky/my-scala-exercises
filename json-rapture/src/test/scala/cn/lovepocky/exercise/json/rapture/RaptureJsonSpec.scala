package cn.lovepocky.exercise.json.rapture

import org.scalatest.FlatSpec
import rapture.data.MutableCell

class RaptureJsonSpec extends FlatSpec {

  "rapture json" should "work" in {}

  it should "use different backend; to/from backend instance" in {
    import rapture.json._
    val j_jackson = {
      import rapture.json.jsonBackends.jackson._

      json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    }

    val j_circe = {
      import rapture.json.jsonBackends.circe._
      json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""
    }

//    val j_jackson_underlying = j_jackson.$normalize.asInstanceOf[com.fasterxml.jackson.databind.node.ObjectNode]
//    val j_circe_underlying   = j_circe.$normalize.asInstanceOf[io.circe.Json]
    val j_jackson_underlying = j_jackson.$root.value.asInstanceOf[com.fasterxml.jackson.databind.node.ObjectNode]
    val j_circe_underlying   = j_circe.$root.value.asInstanceOf[io.circe.Json]
    println(j_jackson_underlying)
    println(j_circe_underlying)

    /**
      * json
      * - root.value => backend value
      * - path
      * - ast
      */
    val j_jackson_return = Json.construct(MutableCell(j_jackson_underlying), Vector())(rapture.json.jsonBackends.jackson.implicitJsonAst)
    val j_circe_return   = Json.construct(MutableCell(j_circe_underlying), Vector())(rapture.json.jsonBackends.circe.implicitJsonAst)
    println(j_jackson_return)
    println(j_circe_return)

    println("over")
  }

  it should "use in vertx" in {
    import io.vertx.core.json.{JsonObject => VJsonObject, Json => VJson}
    import io.vertx.lang.scala.json.{Json => VSJson}
    import rapture.json.jsonBackends.jackson._
    import rapture.json._

    /*io.vertx.core.json.Json.mapper.registerModule(
      com.fasterxml.jackson.module.scala.DefaultScalaModule
    )*/
    val vsjson = VSJson.obj("name" -> "abc", "age" -> 18, "d" -> 1.123, "arr" -> VSJson.arr(1, 2, 3), "boo" -> true)

    val j_jackson: Json = json"""{ "name": "abc", "age": 18, "arr": [ 1,2,3] }"""

    val j_vsjson = Json.construct(MutableCell(vsjson), Vector())(rapture.json.jsonBackends.vertx.implicitJsonAst)
    println(j_vsjson)
    j_jackson.toBareString
    j_vsjson.toBareString

    {
      val vsjson   = VSJson.obj("b" -> true, "name" -> "abc")
      val j_vsjson = Json.construct(MutableCell(vsjson), Vector())(rapture.json.jsonBackends.vertx.implicitJsonAst)
      println(j_vsjson.toString)
    }

    println("over")
  }

}
