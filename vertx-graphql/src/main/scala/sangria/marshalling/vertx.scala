package sangria.marshalling

import io.vertx.lang.scala.json.{Json => VJson, _}
import sangria.marshalling.vertx.VertxResultMarshaller

object vertx {

  implicit object VertxResultMarshaller extends ResultMarshaller {
    type VertxJson           = AnyRef
    override type Node       = VertxJson
    override type MapBuilder = ArrayMapBuilder[Node]

    override def emptyMapNode(keys: Seq[String]): ArrayMapBuilder[Node] = new ArrayMapBuilder[Node](keys)

    override def addMapNodeElem(builder: ArrayMapBuilder[Node],
                                key: String,
                                value: VertxResultMarshaller.Node,
                                optional: Boolean): ArrayMapBuilder[Node] = builder.add(key, value)

    override def mapNode(builder: ArrayMapBuilder[Node]): VertxResultMarshaller.Node = VJson.obj(builder.toSeq: _*)

    override def mapNode(keyValues: Seq[(String, Node)]): VertxResultMarshaller.Node = VJson.obj(keyValues: _*)

    override def arrayNode(values: Vector[Node]): VertxResultMarshaller.Node = VJson.arr(values: _*)

    override def optionalArrayNodeValue(value: Option[Node]): VertxResultMarshaller.Node = value match {
      case Some(v) ⇒ v
      case None    ⇒ null
    }

    override def scalarNode(value: Any, typeName: String, info: Set[ScalarValueInfo]): VertxResultMarshaller.Node = value match {
      case v: String     ⇒ v
      case v: Boolean    ⇒ Boolean.box(v)
      case v: Int        ⇒ Int.box(v)
      case v: Long       ⇒ Long.box(v)
      case v: Float      ⇒ Float.box(v)
      case v: Double     ⇒ Double.box(v)
      case v: BigInt     ⇒ v
      case v: BigDecimal ⇒ v
      case v             ⇒ throw new IllegalArgumentException("Unsupported scalar value: " + v)
    }

    override def enumNode(value: String, typeName: String): VertxResultMarshaller.Node = value

    override def nullNode: VertxResultMarshaller.Node = null

    override def renderCompact(node: VertxResultMarshaller.Node): String = node match {
      case x: JsonObject => x.encode()
      case x: JsonArray  => x.encode()
    }

    override def renderPretty(node: VertxResultMarshaller.Node): String = node match {
      case x: JsonObject => x.encodePrettily()
      case x: JsonArray  => x.encodePrettily()
    }

  }

}
