package github.dboroujerdi.sched.app.parse.html

import github.dboroujerdi.sched.api.model.{InPlayEvent, ScheduledEvent, Score}
import github.dboroujerdi.sched.app.parse.{Fields, RowParser}
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, elementList, text}

object InPlay extends FieldParsing {

  val parse: RowParser = EventElementParser.parseRow(parseFields)(extractFields)

  private def parseFields(fields: Fields): Option[ScheduledEvent] = {
    for {
      teamNames <- parseTeamNames(fields._3)
      score <- parseScore(fields._4)
      time = fields._1
      matchId <- parseIdFromUrl(fields._2)
    } yield InPlayEvent(matchId, teamNames._1, teamNames._2, "Football", time, Score(score))
  }

  private def extractFields(elem: Element): Option[Fields] = {
    (elem >?> elementList("td"))
      .flatMap {
        case time :: score :: event :: _ => Some((time, score, event))
        case _ => None
      }
      .map {
        case (timeRow, scoreRow, eventRow) =>
          (timeRow >?> text("a"), eventRow >?> (attr("href")("a"), text("a")), scoreRow >?> text("a"))
      }
      .flatMap {
        case (Some(t), (Some(l), Some(n)), Some(s)) => Some((t, l, n, s))
        case _ => None
      }
  }
}
