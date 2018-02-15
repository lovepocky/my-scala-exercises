package cn.lovepocky.exercise.async

import org.scalatest.FlatSpec

import scala.concurrent.duration.Duration

class ScalaAsyncSpec extends FlatSpec {

  /**
    *
    * {{{
    * - https://github.com/scala/scala-async
    * - [Scala 中的异步事件处理](https://www.ibm.com/developerworks/cn/java/j-jvmc4/index.html)
    * }}}
    */
  "scala-async" should "work" in {
    import concurrent.ExecutionContext.Implicits.global
    import scala.async.Async._
    println(s"in main, thread name = ${Thread.currentThread().getName}")
    val f = async {
      println(s"in async, thread name = ${Thread.currentThread().getName}")
      val f1 = async {
        val cur_thread_name = Thread.currentThread().getName
        println(s"in f1, thread name = $cur_thread_name")
        Thread.sleep(1000)
        println(s"[f1] thread sleep complete")
        cur_thread_name
      }
      val f2 = async {
        val cur_thread_name = Thread.currentThread().getName
        println(s"[f2] thread name = $cur_thread_name")
        Thread.sleep(500)
        println(s"[f2] thread sleep complete")
        cur_thread_name
      }
      await(f2)
      await(f1)

      println("in async, complete")

    }
    concurrent.Await.result(f, Duration.Inf)
  }

}
