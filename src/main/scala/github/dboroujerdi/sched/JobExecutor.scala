package github.dboroujerdi.sched

import java.util.concurrent.{ScheduledThreadPoolExecutor, TimeUnit}

trait JobExecutor {
  val ex = new ScheduledThreadPoolExecutor(1)

  val task = new Runnable {
    def run() = {
      executeJob()
    }
  }

  ex.scheduleAtFixedRate(task, 1, 10, TimeUnit.SECONDS)

  def executeJob()
}
