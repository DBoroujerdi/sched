package github.dboroujerdi.sched.poller

import github.dboroujerdi.sched.config.ConfigComponent

trait PollerComponent {
  val taskExecutor: TaskExecutor
}

trait PollExecutorComponent extends PollerComponent {
  self: ConfigComponent =>

  lazy val taskExecutor = new ScheduledTaskExecutor(config.getInt("schedule.scrape.interval"))
}
