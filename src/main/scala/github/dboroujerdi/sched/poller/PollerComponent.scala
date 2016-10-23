package github.dboroujerdi.sched.poller

import com.typesafe.config.Config
import github.dboroujerdi.sched.config.ConfigComponent

trait PollerComponent {
  val config: Config
  val taskExecutor: TaskExecutor
}

trait PollExecutorComponent extends PollerComponent {
  self: ConfigComponent =>

  lazy val taskExecutor = new ScheduledTaskExecutor(config.getInt("schedule.scrape.interval"))
}
