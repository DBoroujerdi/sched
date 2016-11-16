package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.infrastructure.DefaultActorSystemComponent
import github.dboroujerdi.sched.parse.stream.StreamParseComponent
import github.dboroujerdi.sched.schedule.DefaultScheduleFacadeComponent
import github.dboroujerdi.sched.scraping.WebDocumentFetcherComponent
import github.dboroujerdi.sched.web.WebServerComponent

trait Application extends DefaultActorSystemComponent
  with WebDocumentFetcherComponent
  with StreamParseComponent
  with WebServerComponent
  with DefaultScheduleFacadeComponent
  with DefaultConfigComponent {

  startServer()

  sys.addShutdownHook(system.terminate())
}


object DefaultApplication extends App with Application
