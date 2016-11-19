package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.api.model.ScheduledEvent
import net.ruippeixotog.scalascraper.model.Element

trait ParseError

case class ExceptionalScrapeError(elem: Element, ex: Exception) extends ParseError
case class NoParseError(elem: Element) extends ParseError

case class ElementFields(time: String, url: String, name: String, sport: String)

object Types {
  type RawSchedule = Seq[Option[ElementFields]]
  type RawFields = (Option[String], (Option[String], Option[String]), Option[String])
  type ErrorOrEvent = ParseError Xor ScheduledEvent
}
