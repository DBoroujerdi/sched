package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.infrastructure.DefaultActorSystemComponent
import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse.SynchronousParserComponent
import github.dboroujerdi.sched.parse.pool.ParallelParserComponent
import github.dboroujerdi.sched.parse.stream.StreamParseComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{WebBrowserComponent, WebScraperComponent}
import github.dboroujerdi.sched.sse.{StreamPublisherComponent, WebServerComponent}

import scala.concurrent.Future

trait Application extends PollExecutorComponent
  with DefaultActorSystemComponent
  with WebScraperComponent
  with WebBrowserComponent
  with StreamParseComponent
  with StreamPublisherComponent
  with WebServerComponent
  with DefaultConfigComponent {

  taskExecutor.start { () =>
    println("Scrape task executing..")

    val events: Future[Schedule] = scraper.scrape(config.getString("schedule.scrape.url"))

    events.onSuccess {
      case schedule => publish(schedule)
        // todo is this exhaustive?
    }
  }

  startServer()
}

object DefaultApplication extends App with Application