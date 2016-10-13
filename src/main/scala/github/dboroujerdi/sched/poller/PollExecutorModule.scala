package github.dboroujerdi.sched.poller

import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.poller.TaskExecutor.Task

trait PollExecutorModule extends PollerModule
  with ConfigComponent {

  lazy val poller = new TaskExecutor(task, config.getInt("schedule.scrape.interval"))

  val task: Task
}
