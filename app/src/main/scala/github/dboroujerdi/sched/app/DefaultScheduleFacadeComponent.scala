package github.dboroujerdi.sched.app

import github.dboroujerdi.sched.api.ScheduleFacadeComponent
import github.dboroujerdi.sched.api.model.Types.{FutureMaybe, Schedule}
import github.dboroujerdi.sched.app.config.ConfigComponent
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.app.parse.ParserComponent
import github.dboroujerdi.sched.app.scraping.DocumentFetcherComponent

import cats.implicits._

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
