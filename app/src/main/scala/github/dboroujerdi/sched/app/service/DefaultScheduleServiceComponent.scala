package github.dboroujerdi.sched.app.service

import cats.implicits._
import github.dboroujerdi.sched.api.ScheduleService.FutureMaybe
import github.dboroujerdi.sched.api.ScheduleServiceComponent
import github.dboroujerdi.sched.api.model.Types.Schedule
import github.dboroujerdi.sched.app.config.ConfigComponent
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.app.parse.ParserComponent
import github.dboroujerdi.sched.app.scraping.DocumentFetcherComponent

trait DefaultScheduleServiceComponent extends ScheduleServiceComponent {
  this: DocumentFetcherComponent
    with ConfigComponent
    with ParserComponent
    with ActorSystemComponent =>

  object ScheduleService extends ScheduleService {

    def preMatch(): FutureMaybe[Schedule] = fetch(preMatchUrl, parsers.preMatchParser)
    def inPlay(): FutureMaybe[Schedule] = fetch(inPlayUrl, parsers.inPlayParser)

    private def fetch(url: String, parser: Parser): FutureMaybe[Schedule] = {
      documentFetcher
        .fetchDocument(url)
        .flatMap(parser)
    }
  }

  val scheduleService: ScheduleService = ScheduleService
}
