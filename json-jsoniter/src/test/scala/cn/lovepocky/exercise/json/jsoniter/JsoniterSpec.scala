package cn.lovepocky.exercise.json.jsoniter

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.AsyncWordSpec

class JsoniterSpec extends AsyncWordSpec with LazyLogging {
  import JsoniterSpec._
  import com.github.plokhotnyuk.jsoniter_scala.macros._
  import com.github.plokhotnyuk.jsoniter_scala.core._

  "jsoniter" should {
    "work" in {
      val user = readFromArray("""{"name":"John","devices":[{"id":1,"model":"HTC One X"}]}""".getBytes("UTF-8"))
      val json = writeToArray(User(name = "John", devices = Seq(Device(id = 2, model = "iPhone X"))))

      logger.info(user.toString)
      logger.info(json.map(_.toChar).mkString)
      assert(true)
    }
  }
}

object JsoniterSpec {
  import com.github.plokhotnyuk.jsoniter_scala.macros._
  import com.github.plokhotnyuk.jsoniter_scala.core._

  case class Device(id: Int, model: String)

  case class User(name: String, devices: Seq[Device])

  implicit val codec: JsonValueCodec[User] = JsonCodecMaker.make[User](CodecMakerConfig())
}
