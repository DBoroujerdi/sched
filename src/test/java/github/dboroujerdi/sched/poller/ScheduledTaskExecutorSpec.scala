package github.dboroujerdi.sched.poller

import org.scalatest.{AsyncFunSpecLike, FunSpec, Matchers}

import scala.concurrent.Promise

class ScheduledTaskExecutorSpec extends Matchers
  with AsyncFunSpecLike {

  describe("A ScheduledTaskExecutor") {

    val interval = 1
    val ex = new ScheduledTaskExecutor(interval)

    it("should eventually execute") {
      val p = Promise[String]()
      ex.start(() =>
        p.success("Hello")
      )

      val f = p.future
      f.map { res => assert(res == "Hello") }
    }
  }
}
