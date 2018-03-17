package cn.lovepocky.exercise.graphql

import org.scalatest.{AsyncWordSpec, WordSpec}

class SangriaSpec extends AsyncWordSpec {

  "sangria" should {

    import SangriaExample._

    "execute query" in {
      import sangria.macros._

      val query =
        graphql"""
          query MyProduct {
            product(id: "2") {
              name
              description

              picture(size: 500) {
                width, height, url
              }
            }

            products {
              name
            }
          }
        """

      import sangria.execution._
      import sangria.marshalling.circe.CirceInputUnmarshaller
      import sangria.marshalling.vertx.VertxResultMarshaller

      import io.vertx.lang.scala.json.{JsonObject, JsonArray}
      import rapture.json.{Json => RJson, _}
      import rapture.data._
      import formatters.humanReadable._
      import rapture.json.jsonBackends.vertx._

      Executor
        .execute(schema, query, new ProductRepo)
        .map { res =>
          println(
            RJson.format(fromJson(res.asInstanceOf[JsonObject]))
          )
          assert(true)
        }
    }

  }

}

object SangriaSpec {

  import sangria.schema._

}
