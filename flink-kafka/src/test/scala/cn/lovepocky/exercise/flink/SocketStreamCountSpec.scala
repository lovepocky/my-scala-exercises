package cn.lovepocky.exercise.flink

import org.scalatest.AsyncWordSpec

import scala.concurrent.Future

class SocketStreamCountSpec extends AsyncWordSpec {

  "flink socket stream" should {
    "work" in {
      val app = new SocketStreamCount

      app.main(
        Array("localhost", "9999")
      )
      assert(true)
    }
  }

}
