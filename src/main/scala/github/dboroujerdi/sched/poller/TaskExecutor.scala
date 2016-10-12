package github.dboroujerdi.sched.poller

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

import github.dboroujerdi.sched.poller.TaskExecutor.Task

import scala.util.{Failure, Success, Try}

class TaskExecutor(task: Task) {

  val ex = new ScheduledThreadPoolExecutor(1)

  val runnable = new Runnable {
    def run() = {
      Try(task()) match {
        case Failure(reason) => reason.printStackTrace()
      }
    }
  }

  def start() = {
    // todo: externalise configuration
    ex.scheduleAtFixedRate(runnable, 1, 10, TimeUnit.SECONDS)
  }
}

object TaskExecutor {
  type Task = Unit => Unit

  def apply(task: Task) = {
    new TaskExecutor(task)
  }
}
