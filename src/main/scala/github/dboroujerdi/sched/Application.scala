package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.infrastructure.DefaultActorSystemComponent
import github.dboroujerdi.sched.parse.pool.ParallelParserComponent
import github.dboroujerdi.sched.parse.stream.StreamParseComponent
import github.dboroujerdi.sched.schedule.DefaultScheduleFacadeComponent
import github.dboroujerdi.sched.scraping.WebDocumentFetcherComponent
import github.dboroujerdi.sched.server.{StreamPublisherComponent, WebServerComponent}

trait Application extends DefaultActorSystemComponent
  with WebDocumentFetcherComponent
  with StreamParseComponent
  with StreamPublisherComponent
  with WebServerComponent
  with DefaultScheduleFacadeComponent
  with DefaultConfigComponent {

  startServer()
}

object DefaultApplication extends App with Application