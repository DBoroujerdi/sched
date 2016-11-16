package github.dboroujerdi.sched.api.model

import github.dboroujerdi.sched.api.model.Types.{MatchId, Sport}
import org.joda.time.DateTime

object Types {
  type Sport = String
  type MatchId = String
  type Schedule = Seq[ScheduledEvent]
}

case class ScheduledEvent(id: MatchId, home: String, away: String, time: DateTime, sport: Sport)
