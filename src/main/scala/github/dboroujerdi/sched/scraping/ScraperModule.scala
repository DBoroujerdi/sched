package github.dboroujerdi.sched.scraping

import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.poller.PollerModule
import github.dboroujerdi.sched.poller.TaskExecutor._

trait ScraperModule {
  this: BrowserComponent with ConfigComponent with PollerModule =>

  val task: Task = Unit => {
    val doc = browser.fetchDocument(config.getString("schedule.scrape.url"))
    println(ScheduleParser.parseRows(doc))
  }
}


