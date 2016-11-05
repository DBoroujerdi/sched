package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}
import github.dboroujerdi.sched.sse.{DefaultActorSystemComponent, WebServerComponent, StreamPublisherComponent}

trait Application extends PollExecutorComponent
  with WebScraperComponent
  with WebBrowserComponent
  with DefaultConfigComponent
  with DefaultActorSystemComponent
  with StreamPublisherComponent
  with WebServerComponent {

  taskExecutor.start { () =>
    val events = scraper.scrape(config.getString("schedule.scrape.url"))
    publish(events)
  }

  startServer()
}

object DefaultApplication extends App with Application