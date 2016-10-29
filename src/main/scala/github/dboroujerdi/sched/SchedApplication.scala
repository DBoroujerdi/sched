package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}

object SchedApplication extends App
  with PollExecutorComponent
  with WebScraperComponent
  with WebBrowserComponent
  with DefaultConfigComponent {

  taskExecutor.start { () =>
    val events = scraper.scrape(config.getString("schedule.scrape.url"))

    println(events)

    // todo send to sse stream
  }
}
