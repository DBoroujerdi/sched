package github.dboroujerdi.sched.poller

import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Seconds, Span}
import org.scalatest.{FunSpecLike, Matchers}

import scala.concurrent.Promise

class ScheduledTaskExecutorSpec extends Matchers
  with FunSpecLike
  with ScalaFutures {

  describe("A ScheduledTaskExecutor") {

    val interval = 1
    val ex = new ScheduledTaskExecutor(interval)

    it("should eventually execute") {
      val p = Promise[String]()
      ex.start(p.success("Hello"))

      val f = p.future
      whenReady(f, Timeout(Span(2, Seconds))) { res =>
        assert(res == "Hello")
      }

      ex.stop()
    }
  }
}
