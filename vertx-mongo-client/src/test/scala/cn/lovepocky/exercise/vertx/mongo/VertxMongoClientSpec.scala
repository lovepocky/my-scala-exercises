package cn.lovepocky.exercise.vertx.mongo

import org.joda.time.DateTime
import org.scalatest.AsyncWordSpec

class VertxMongoClientSpec extends AsyncWordSpec {

  import io.vertx.scala.core._
  import io.vertx.scala.ext.mongo._
  import io.vertx.lang.scala.json._

  import rapture.json.{Json => RJson, _}
  import rapture.json.jsonBackends.vertx._

  "vertx mongo client" should {
    lazy val vertx = Vertx.vertx()

    lazy val config = json"""
            {
              "db_name": "test",
              "host": "192.168.3.199"
            }
          """.$root.value.asInstanceOf[JsonObject]
    lazy val client = MongoClient.createShared(vertx, config)
    "insert" in {
      val doc =
        json"""
              {
                "_id": {
                  "$$oid": ${org.bson.types.ObjectId.get().toHexString}
                },
                "name": "hello",
                "age": 20,
                "time": {
                  "$$date": ${DateTime.now().toString()}
                }
              }
            """.$root.value.asInstanceOf[JsonObject]
      client.insertFuture("vertx", doc).map { res =>
        println(res)
        assert(true)
      }
    }

    "find" in {
      client.findFuture("vertx", Json.emptyObj()).map { res =>
        res.foreach { doc =>
          println(doc)
        }
        assert(true)
      }
    }

    "findOne" in {
      client.findOneFuture("vertx", Json.obj(), Some(Json.obj("name" -> 1))).map { res =>
        println(res)
        assert(true)
      }
    }

    "aggregate" in {
      val query = json"""
          [
              {
                  "$$project": {
                      "name": 1,
                      "time": 1
                  }
              }
          ]
        """.$root.value.asInstanceOf[JsonArray]
      client
        .runCommandFuture("aggregate", Json.obj("pipeline" -> query, "aggregate" -> "vertx"))
        .map { res =>
          println(res)
          (fromJson(res) \ "result").as[Seq[RJson]].foreach(println)
          assert(true)
        }
    }

  }

}
