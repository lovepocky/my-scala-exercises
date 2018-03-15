package cn.lovepocky.exercise.airframe.di

import org.scalatest.FlatSpec

class AirframeDISpec extends FlatSpec {

  import wvlet.airframe._
  import AirframeDISpec._

  lazy val d = newDesign
    .bind[Parameter]
    .toInstance(new Parameter("di"))
  "airframe di" should "with-session" in {
    d.withSession { s =>
      s.build[App]
    }
  }

  it should "use session" in {
    val session = d.newSession
    session.build[App]
    session.build[App]
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
