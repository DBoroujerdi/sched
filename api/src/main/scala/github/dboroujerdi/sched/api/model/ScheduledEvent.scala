package github.dboroujerdi.sched.api.model

import github.dboroujerdi.sched.api.model.Types.{MatchId, MatchTime, Sport}
import org.joda.time.DateTime

object Types {
  type Sport = String
  type MatchId = String
  type Schedule = Seq[ScheduledEvent]
  type MatchTime = String
}

case class Score(home: Int, away: Int)

object Score {
  def apply(tuple: (Int, Int)): Score = Score(tuple._1, tuple._2)
}

trait ScheduledEvent

case class PreMatchEvent(id: MatchId, home: String, away: String, sport: Sport, time: DateTime) extends ScheduledEvent
case class InPlayEvent(id: MatchId, home: String, away: String, sport: Sport, matchTime: MatchTime, score: Score) extends ScheduledEvent
