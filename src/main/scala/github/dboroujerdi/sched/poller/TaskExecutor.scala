package github.dboroujerdi.sched.poller

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import scala.util.{Failure, Try}

trait TaskExecutor {
  def start(task: => Unit): Unit
  def stop(): Unit
}

class ScheduledTaskExecutor(interval: Int) extends TaskExecutor {
  private[this] val ex = new ScheduledThreadPoolExecutor(1)

  private def createRunnable(task: => Unit): Runnable = new Runnable {
    def run() = {
      Try(task) match {
        case Failure(reason) => reason.printStackTrace()
        case _ =>
      }
    }
  }

  def start(task: => Unit) = {
    val runnable = createRunnable(task)
    ex.scheduleAtFixedRate(runnable, 1, interval, TimeUnit.SECONDS)
  }

  def stop(): Unit = {
    ex.shutdown()
  }
}
