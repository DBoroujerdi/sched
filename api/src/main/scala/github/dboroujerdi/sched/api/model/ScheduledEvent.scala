package github.dboroujerdi.sched.api.model

import cats.data.OptionT
import github.dboroujerdi.sched.api.model.Types.{MatchId, Sport}
import org.joda.time.DateTime

import scala.concurrent.Future

object Types {
  type Sport = String
  type MatchId = String
  type Schedule = Seq[ScheduledEvent]
  type FutureMaybe[T] = OptionT[Future, T]
}

case class ScheduledEvent(id: MatchId, home: String, away: String, time: DateTime, sport: Sport)
