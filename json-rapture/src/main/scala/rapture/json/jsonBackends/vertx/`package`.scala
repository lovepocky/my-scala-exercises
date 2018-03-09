package rapture.json.jsonBackends.vertx

import rapture.json._

object `package` extends Extractors with Serializers {
  implicit val implicitJsonAst = VertxJsonAst //CirceAst
  implicit val implicitJsonStringParser = //CirceParser
}