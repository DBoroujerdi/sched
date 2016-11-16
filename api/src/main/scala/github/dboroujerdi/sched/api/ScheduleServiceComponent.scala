package github.dboroujerdi.sched.api

import cats.data.OptionT
import github.dboroujerdi.sched.api.ScheduleService.FutureMaybe
import github.dboroujerdi.sched.api.model.Types.Schedule

import scala.concurrent.Future

trait ScheduleServiceComponent {

  trait ScheduleService {
    def fetch(): FutureMaybe[Schedule]
  }

  val scheduleService: ScheduleService
}

object ScheduleService {
  type FutureMaybe[T] = OptionT[Future, T]
}


