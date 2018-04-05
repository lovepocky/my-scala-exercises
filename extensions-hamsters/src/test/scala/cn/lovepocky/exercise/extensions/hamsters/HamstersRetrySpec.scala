package cn.lovepocky.exercise.extensions.hamsters

import com.typesafe.scalalogging.LazyLogging
import org.scalatest.AsyncWordSpec

import scala.util.Try

class HamstersRetrySpec extends AsyncWordSpec with LazyLogging {
  "hamsters retry" should {
    "work" in {
      import io.github.hamsters.Retry

      var count = 0

      val logErrorsFunction = (errorMessage: String) => println(errorMessage)

      val r: Try[Int] = Retry(maxTries = 3, logErrorsFunction) {
        if (count < 3) {
          println(s"execute count = ${count}")
          count += 1
          throw new Exception(s"inner exception, count = ${count}")
        }
        1 + 1
      }

      println(s"res = ${r.failed.get.printStackTrace()}")

      assert(true)
    }
    "work with timeout" in {
      import io.github.hamsters.jvm.Retry
      val logErrorsFunctionMock = (errorMessage: String) => println(errorMessage)
      var count = 0

      val result = Retry.withWait(3, 50, logErrorsFunctionMock) {
        println(s"count = $count")
        Thread.sleep(1000)
        1 + 1
      }
      result
        .map { r =>
          println(s"res in map = ${r}")
          assert(true)
        }
        .recover {
          case e: Exception =>
            e.printStackTrace()
            assert(true)
        }
    }
  }

}
