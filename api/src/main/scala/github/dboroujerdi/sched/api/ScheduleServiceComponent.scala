package github.dboroujerdi.sched.api

import github.dboroujerdi.sched.api.model.Types.{FutureMaybe, Schedule}

trait ScheduleServiceComponent {
  trait ScheduleService {
    def fetch(): FutureMaybe[Schedule]
  }

  val scheduleService: ScheduleService
}


