package sangria.marshalling

import org.scalatest.WordSpec
import org.scalatest.Matchers

class CirceMarshallingSpec extends WordSpec with Matchers {

  import sangria.marshalling.circe._
  import sangria.marshalling.vertx._
  import io.circe.{Json => CJson, _}
  import io.vertx.lang.scala.json.{Json => VJson, _}

  "circe marshalling CirceResultMarshaller" should {

    "emptyMapNode" in {
      val res = CirceResultMarshaller.emptyMapNode(Seq("a", "b"))
      println(res.toList)
      println(res)
    }

    "addMapNodeElem" in {
      val res = CirceResultMarshaller.addMapNodeElem(
        CirceResultMarshaller.emptyMapNode(Seq("a", "b")),
        "a",
        CJson.obj("hello" -> CJson.fromLong(1)),
        false
      )
      println(res)
    }

    "mapNode (builder)" in {
      def handle(marshaller: ResultMarshaller, value: Any): marshaller.Node = {
        marshaller.mapNode(
          marshaller.addMapNodeElem(
            marshaller.emptyMapNode(Seq("a", "b")),
            "a",
            value.asInstanceOf[marshaller.Node],
            false
          )
        )
      }

      val res_circe = handle(CirceResultMarshaller, CJson.obj("hello" -> CJson.fromLong(1)))
      val res_vertx = handle(VertxResultMarshaller, VJson.obj("hello" -> 1L))
      println(res_circe.noSpaces)
      println(res_vertx)
      assert(res_circe.noSpaces == res_vertx.toString)
    }

    "mapNode (kvs)" in {
      def handle(marshaller: ResultMarshaller, value: Seq[(String, Any)]): marshaller.Node = {
        marshaller.mapNode(value.asInstanceOf[Seq[(String, marshaller.Node)]])
      }
      val res_circe = handle(CirceResultMarshaller, Seq("a" -> CJson.obj("hello" -> CJson.fromLong(1))))
      val res_vertx = handle(VertxResultMarshaller, Seq("a" -> VJson.obj("hello" -> 1L)))
      println(res_circe.noSpaces)
      println(res_vertx)
      assert(res_circe.noSpaces == res_vertx.toString)
    }

    "arrayNode" in {
      def handle(marshaller: ResultMarshaller, value: Vector[Any]): marshaller.Node = {
        marshaller.arrayNode(value.asInstanceOf[Vector[marshaller.Node]])
      }
      val res_circe = handle(CirceResultMarshaller, Vector(CJson.obj("a" -> CJson.obj("hello" -> CJson.fromLong(1)))))
      val res_vertx = handle(VertxResultMarshaller, Vector(VJson.obj("a" -> VJson.obj("hello" -> 1L))))
      printAndAssert(res_circe, res_vertx)
    }

    "optionalArrayNodeValue" in {
      def handle(marshaller: ResultMarshaller, value: Option[Any]): marshaller.Node = {
        marshaller.optionalArrayNodeValue(value.asInstanceOf[Option[marshaller.Node]])
      }
      val res_circe  = handle(CirceResultMarshaller, Some(CJson.obj("a" -> CJson.obj("hello" -> CJson.fromLong(1)))))
      val res_circe2 = handle(CirceResultMarshaller, None)
      val res_vertx  = handle(VertxResultMarshaller, Some(VJson.obj("a" -> VJson.obj("hello" -> 1L))))
      val res_vertx2 = handle(VertxResultMarshaller, None)
      printAndAssert(res_circe, res_vertx)
      printAndAssert(res_circe2, res_vertx2)
    }

    "scalarNode" in {
      def handle(marshaller: ResultMarshaller, value: Any): marshaller.Node = {
        marshaller.scalarNode(value, "", Set())
      }
      val res_circe = handle(CirceResultMarshaller, 1L)
      val res_vertx = handle(VertxResultMarshaller, 1L)
      printAndAssert(res_circe, res_vertx)
    }

    "enumNode" in {
      def handle(marshaller: ResultMarshaller, value: Any): marshaller.Node = {
        marshaller.enumNode("hello", "")
      }
      val res_circe = handle(CirceResultMarshaller, 1L)
      val res_vertx = handle(VertxResultMarshaller, 1L)
      println(CJson.fromString("hello").toString())
      println(res_circe.isString)
      println(VJson.arr("string").encode())
    }

    def printAndAssert(c: CJson, v: Any) = {
      println(c.noSpaces)
      println(v)
      (if (v == null) "null" else v.toString) shouldBe c.noSpaces
    }

  }

}
