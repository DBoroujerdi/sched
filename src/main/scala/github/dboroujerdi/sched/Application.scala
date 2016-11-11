package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.infrastructure.DefaultActorSystemComponent
import github.dboroujerdi.sched.parse.SynchronousParserComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}
import github.dboroujerdi.sched.sse.{StreamPublisherComponent, WebServerComponent}

trait Application extends PollExecutorComponent
  with WebScraperComponent
  with WebBrowserComponent
  with SynchronousParserComponent
  with DefaultActorSystemComponent
  with StreamPublisherComponent
  with WebServerComponent
  with DefaultConfigComponent {

  taskExecutor.start { () =>
    println("Scrape task executing..")

    val events = scraper.scrape(config.getString("schedule.scrape.url"))

    publish(events)
  }

  startServer()
}

object DefaultApplication extends App with Application