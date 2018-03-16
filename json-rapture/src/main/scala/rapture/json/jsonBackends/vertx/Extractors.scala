package rapture.json.jsonBackends.vertx

import rapture.json._
import rapture.data._

private[vertx] trait Extractors {
  implicit val vertxJsonNodeExtractor: JsonCastExtractor[Any] = JsonCastExtractor(VertxJsonAst, DataTypes.Any)
}
