package github.dboroujerdi.sched.parse

import cats.data.Xor
import github.dboroujerdi.sched.model.ScheduledEvent
import net.ruippeixotog.scalascraper.model.Element

trait ScrapeError

case class ExceptionalScrapeError(elem: Element, ex: Exception) extends ScrapeError
case class NoParseError(elem: Element) extends ScrapeError

case class ElementFields(time: String, url: String, name: String, sport: String)

object Types {
  type RawSchedule = Seq[Option[ElementFields]]
  type RawFields = (Option[String], (Option[String], Option[String]), Option[String])
  type ErrorOrEvent = ScrapeError Xor ScheduledEvent
}
