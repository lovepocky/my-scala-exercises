package rapture.json.jsonBackends.vertx

import rapture.data.Parser
import rapture.json._

object `package` extends Extractors with Serializers {
  implicit val implicitJsonAst: JsonAst                               = VertxJsonAst
  implicit lazy val implicitJsonStringParser: Parser[String, JsonAst] = VertxJsonParser
}
