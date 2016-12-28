package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.api.model.ScheduledEvent
import net.ruippeixotog.scalascraper.model.Element

trait ParseError

case class ExceptionalScrapeError(elem: Element, ex: Exception) extends ParseError
case class NoParseError(elem: Element) extends ParseError

object Types {
  type RawFields = (Option[String], (Option[String], Option[String]), Option[String])
  type ErrorOrEvent = ParseError Xor ScheduledEvent
}
