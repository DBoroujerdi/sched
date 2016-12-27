package github.dboroujerdi.sched.app.parse.html.prematch

import java.net.URL

import cats.data.Xor
import github.dboroujerdi.sched.api.model.Types._
import github.dboroujerdi.sched.api.model.{PreMatchEvent, ScheduledEvent}
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import github.dboroujerdi.sched.app.parse.html.prematch.EventElementParser.TeamsNames
import github.dboroujerdi.sched.app.parse.html.{ElementFields, ExceptionalScrapeError, NoParseError}
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import org.joda.time.DateTime

import scala.util.{Failure, Success, Try}

import github.dboroujerdi.sched.app.parse.util._

object EventElementParser {
  type EventElement = Element
  type TeamsNames = (String, String)
}

class EventElementParser {
  this: TimeParser =>

  def parseElement(element: Element): ErrorOrEvent = {
    try {
      pullOutElementFields(element)
        .flatMap(parseFields) match {
        case Some(event) => Xor.Right(event)
        case None => Xor.Left(NoParseError(element))
      }
    } catch {
      case ex: Exception =>
        Xor.Left(ExceptionalScrapeError(element, ex))
    }
  }

  private def parseFields(item: ElementFields): Option[ScheduledEvent] = {
    for {
      teamNames <- parseOutTeamNames(item.name)
      time <- parseTime(item.time, DateTime.now())
      matchId <- parseIdFromUrl(item.url)
    } yield PreMatchEvent(matchId, teamNames._1, teamNames._2, item.sport, time)
  }

  private def parseOutTeamNames(name: String): Option[TeamsNames] = {
    name.split(" v ") match {
      case Array(home, away) => Some((home, away))
      case _ => None
    }
  }

  private def parseIdFromUrl(urlS: String): Option[MatchId] = {
    Try(new URL(urlS)) match {
      case Success(url) =>
        (for {
          path <- url.extractPath
          paths = path.splitBy('/').tail
        } yield paths) match {
          case Some(p) if p.length >= 4 => Some(p(4))
          case _ => None
        }
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  private def pullOutElementFields(elem: Element): Option[ElementFields] = {
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
        case (Some(t), (Some(l), Some(n)), Some(s)) => Some(ElementFields(t, l, n, s))
        case _ => None
      }
  }
}