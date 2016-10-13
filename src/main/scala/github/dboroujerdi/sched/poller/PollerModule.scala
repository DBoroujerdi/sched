package github.dboroujerdi.sched.poller

import com.typesafe.config.Config
import github.dboroujerdi.sched.poller.TaskExecutor.Task

trait PollerModule {
  val config: Config
  val task: Task
}
