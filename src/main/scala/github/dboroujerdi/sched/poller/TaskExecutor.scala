package github.dboroujerdi.sched.poller

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import github.dboroujerdi.sched.poller.TaskExecutor.Task

import scala.util.{Failure, Try}

class TaskExecutor(task: Task, interval: Int) {

  private[this] val ex = new ScheduledThreadPoolExecutor(1)

  private[this] val runnable = new Runnable {
    def run() = {
      Try(task()) match {
        case Failure(reason) => reason.printStackTrace()
      }
    }
  }

  def start = {
    ex.scheduleAtFixedRate(runnable, 1, interval, TimeUnit.SECONDS)
  }
}

object TaskExecutor {
  type Task = Unit => Unit

  def apply(task: Task, interval: Int) = {
    new TaskExecutor(task, interval)
  }
}
