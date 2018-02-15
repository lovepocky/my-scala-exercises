package cn.lovepocky.exercise.coroutines

import org.scalatest.FlatSpec

class ScalaCoroutinesSpec extends FlatSpec {

  /**
    * {{{
    * http://storm-enroute.com/coroutines/docs/0.6/101/
    * }}}
    *
    */
  import org.coroutines._

  "scala-coroutines" should "work" in {
    val id = coroutine { (x: Int) =>
      x
    }
    val c = call(id(7))
    c.resume
    assert(c.result == 7)
  }

}
