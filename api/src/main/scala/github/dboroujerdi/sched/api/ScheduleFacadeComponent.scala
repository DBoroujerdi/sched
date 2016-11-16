package github.dboroujerdi.sched.api

import github.dboroujerdi.sched.api.model.Types.{FutureMaybe, Schedule}

trait ScheduleFacadeComponent {
  trait ScheduleFacade {
    def fetch(): FutureMaybe[Schedule]
  }

  val scheduleFacade: ScheduleFacade
}


