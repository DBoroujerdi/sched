package github.dboroujerdi.sched

import github.dboroujerdi.sched.poller.PollerModule
import github.dboroujerdi.sched.poller.TaskExecutor.Task
import github.dboroujerdi.sched.scraping.Scraper

object SchedApplication extends App
  with PollerModule
  with Scraper {

  override val task: Task = Unit => {
    val url: String = "http://sports.williamhill.com/bet/en-gb/betlive/schedule"

    val res = scrape(url)
    println(res)
  }

  poller.start()
}
