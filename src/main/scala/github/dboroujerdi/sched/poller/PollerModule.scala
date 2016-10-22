package github.dboroujerdi.sched.poller

import com.typesafe.config.Config
import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.poller.TaskExecutor.Task

trait PollerModule {
  val config: Config
  val task: Task
}

trait PollExecutorModule extends PollerModule
  with ConfigComponent {

  lazy val poller = new TaskExecutor(task, config.getInt("schedule.scrape.interval"))

  val task: Task
}
