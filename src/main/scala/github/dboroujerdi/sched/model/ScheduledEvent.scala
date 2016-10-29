package github.dboroujerdi.sched.model

import github.dboroujerdi.sched.model.Types.{MatchId, Sport}
import org.joda.time.DateTime

object Types {
  type Sport = String
  type MatchId = String
}

case class ScheduledEvent(id: MatchId, home: String, away: String, time: DateTime, sport: Sport)
