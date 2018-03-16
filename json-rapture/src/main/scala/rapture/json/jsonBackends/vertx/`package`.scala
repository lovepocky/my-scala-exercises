package rapture.json.jsonBackends.vertx

import rapture.data.{MutableCell, Parser}
import rapture.json._

object `package` extends Extractors with Serializers {
  implicit val implicitJsonAst: JsonAst                               = VertxJsonAst
  implicit lazy val implicitJsonStringParser: Parser[String, JsonAst] = VertxJsonParser

  import io.vertx.core.json.{JsonObject, JsonArray}
  def fromJson(json: JsonObject): Json = rapture.json.Json.construct(MutableCell(json), Vector())
  def fromJson(json: JsonArray): Json  = rapture.json.Json.construct(MutableCell(json), Vector())

}
