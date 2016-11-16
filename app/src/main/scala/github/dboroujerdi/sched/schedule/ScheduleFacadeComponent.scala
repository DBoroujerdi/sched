package github.dboroujerdi.sched.schedule

import github.dboroujerdi.sched.FutureMaybe
import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse.ParserComponent
import github.dboroujerdi.sched.scraping.DocumentFetcherComponent
import cats.implicits._
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent

trait ScheduleFacadeComponent {
  trait ScheduleFacade {
    def fetch(): FutureMaybe[Schedule]
  }

  val scheduleFacade: ScheduleFacade
}

trait DefaultScheduleFacadeComponent extends ScheduleFacadeComponent {
  this: DocumentFetcherComponent
    with ConfigComponent
    with ParserComponent
    with ActorSystemComponent =>

  object ScheduleFacade extends ScheduleFacade {

    val url = config.getString("schedule.scrape.url")

    def fetch(): FutureMaybe[Schedule] = {
      documentFetcher.fetchDocument(url).flatMap(document => parser.parse(document))
    }
  }

  val scheduleFacade: ScheduleFacade = ScheduleFacade
}


