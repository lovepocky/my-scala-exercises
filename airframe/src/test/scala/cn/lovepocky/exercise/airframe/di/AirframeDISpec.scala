package cn.lovepocky.exercise.airframe.di

import org.scalatest.{FlatSpec, WordSpec}

class AirframeDISpec extends WordSpec {

  import wvlet.airframe._
  import AirframeDISpec._

  lazy val d = newDesign
    .bind[Parameter]
    .toInstance(new Parameter("di"))
  "airframe di" should {
    "with-session" in {
      d.withSession { s =>
        s.build[App]
      }
    }
    "use session" in {
      val session = d.newSession
      session.build[App]
      session.build[App]
    }

    "cannot inject impl class which have bind" in {
      trait TT {
        def tt: String
      }
      class T {
        val t = bind[TT]
      }

      class TTA extends TT {
        override def tt: String = "tta"
      }

      class TTB extends TT {
        override def tt: String = "ttb"
        val foo                 = bind[FOO]
      }

      class FOO

      val d = newDesign
        .bind[TT]
        .to[TTB]
      d.withSession { s =>
        val x = s.build[TT]
        println(x.tt)
      }

    }
  }

}

object AirframeDISpec {
  import wvlet.airframe._

  class Parameter(val from: String)

  class App {
    val p = bind[Parameter]
    println(p.from)
  }
}
