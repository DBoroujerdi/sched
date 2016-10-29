package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}
import github.dboroujerdi.sched.sse.Protocols
import spray.json._

object SchedApplication extends App
  with PollExecutorComponent
  with WebScraperComponent
  with WebBrowserComponent
  with DefaultConfigComponent
  with Protocols {

  taskExecutor.start { () =>
    val events = scraper.scrape(config.getString("schedule.scrape.url"))

    events.foreach(e => println(e.toJson))

    // todo send to sse stream
  }
}
