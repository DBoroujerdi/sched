package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}
import github.dboroujerdi.sched.sse.{DefaultActorSystemComponent, SseWebServerComponent, StreamPublisherComponent}


object SchedApplication extends App
  with PollExecutorComponent
  with WebScraperComponent
  with WebBrowserComponent
  with DefaultConfigComponent
  with DefaultActorSystemComponent
  with StreamPublisherComponent
  with SseWebServerComponent {

  taskExecutor.start { () =>
    val events = scraper.scrape(config.getString("schedule.scrape.url"))
    publish(events)
  }

  startServer()
}
