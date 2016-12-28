package github.dboroujerdi.sched.app.parse.html

import github.dboroujerdi.sched.api.model.{PreMatchEvent, ScheduledEvent}
import github.dboroujerdi.sched.app.parse._
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, elementList, text}
import org.joda.time.DateTime

object PreMatch extends FieldParsing {

  def parser(timeParser: TimeParser)(element: Element): ErrorOrEvent = {
    EventElementParser.parseRow(parseFields(timeParser))(extractFields)(element)
  }

  private def parseFields(timeParser: TimeParser)(fields: Fields): Option[ScheduledEvent] = {
    for {
      teamNames <- parseTeamNames(fields._3)
      time <- timeParser.parseTime(fields._1, DateTime.now())
      matchId <- parseIdFromUrl(fields._2)
    } yield PreMatchEvent(matchId, teamNames._1, teamNames._2, fields._4, time)
  }

  private def extractFields(elem: Element): Option[Fields] = {
    (elem >?> elementList("td"))
      .flatMap {
        case f :: s :: t :: Nil => Some((f, s, t))
        case _ => None
      }
      .map {
        case (timeRow, gameRow, sportRow) =>
          (timeRow >?> text("span"), gameRow >?> (attr("href")("a"), text("a")), sportRow >?> text("a"))
      }
      .flatMap {
        case (Some(t), (Some(l), Some(n)), Some(s)) => Some((t, l, n, s))
        case _ => None
      }
  }
}
