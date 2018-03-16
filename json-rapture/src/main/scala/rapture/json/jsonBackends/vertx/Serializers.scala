package rapture.json.jsonBackends.vertx

import rapture.json._

private[vertx] trait Serializers {

  implicit val vertxJsonNodeSerializer: DirectJsonSerializer[Any] = DirectJsonSerializer(VertxJsonAst)
}
