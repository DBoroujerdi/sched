package github.dboroujerdi.sched.poller

import github.dboroujerdi.sched.poller.TaskExecutor.Task

trait PollerModule {

  lazy val poller = new TaskExecutor(task)

  // wired in by application module context
  val task: Task
}
