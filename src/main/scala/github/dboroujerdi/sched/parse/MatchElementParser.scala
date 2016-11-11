package github.dboroujerdi.sched.parse

import java.net.URL

import cats.data.Xor
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.model.Types._
import github.dboroujerdi.sched.parse.util.UrlParseUtils
import Types.ErrorOrEvent
import github.dboroujerdi.sched.parse.MatchElementParser.TeamsNames
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr => _, element => _, elementList => _, text => _}
import org.joda.time.DateTime

import scala.util.{Failure, Success, Try}

object MatchElementParser {
  type MatchElement = Element
  type TeamsNames = (String, String)
}

class MatchElementParser(timeParser: TimeParser) {

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
      time <- timeParser.parseTime(item.time, DateTime.now())
      matchId <- parseIdFromUrl(item.url)
    } yield ScheduledEvent(matchId, teamNames._1, teamNames._2, time, item.sport)
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
          path <- UrlParseUtils.extractPath(url)
          paths = UrlParseUtils.splitPath(path)
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