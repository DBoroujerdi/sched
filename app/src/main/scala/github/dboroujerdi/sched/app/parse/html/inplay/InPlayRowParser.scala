package github.dboroujerdi.sched.app.parse.html.inplay

import java.net.URL

import cats.data.Xor
import github.dboroujerdi.sched.api.model.Types.MatchId
import github.dboroujerdi.sched.api.model.{InPlayEvent, ScheduledEvent, Score}
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import github.dboroujerdi.sched.app.parse.html.prematch.EventElementParser.TeamsNames
import github.dboroujerdi.sched.app.parse.html.{ExceptionalScrapeError, NoParseError}
import github.dboroujerdi.sched.app.parse.util.URLUtils._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr, elementList, text}

import scala.util.{Failure, Success, Try}

// todo lots of code duplication here!
object InPlayRowParser {

  def parseRow(element: Element): ErrorOrEvent = {
    // todo this general algo is repeated for pre-match
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

  private def parseFields(fields: (String, String, String, String)): Option[ScheduledEvent] = {
    for {
      teamNames <- parseOutTeamNames(fields._3)
      score <- parseScore(fields._4)
      time = fields._1
      matchId <- parseIdFromUrl(fields._2)
    } yield InPlayEvent(matchId, teamNames._1, teamNames._2, "Football", time, Score.from(score))
  }

  private def parseIdFromUrl(urlS: String): Option[MatchId] = {
    Try(new URL(urlS)) match {
      case Success(url) =>
        (for {
          path <- extractPath(url)
          paths = splitPath(path)
        } yield paths) match {
          case Some(p) if p.length >= 4 => Some(p(4))
          case _ => None
        }
      case Failure(e) =>
        e.printStackTrace()
        None
    }
  }

  private def parseScore(score: String): Option[(String, String)] = {
    score.split("-") match {
      case Array(home, away) => Some((home, away))
      case _ => None
    }
  }

  private def parseOutTeamNames(name: String): Option[TeamsNames] = {
    name.split(" v ") match {
      case Array(home, away) => Some((home.replace('\u00A0',' ').trim(), away.replace('\u00A0',' ').trim()))
      case _ => None
    }
  }

  private def pullOutElementFields(elem: Element): Option[(String, String, String, String)] = {
    (elem >?> elementList("td"))
      .flatMap {
        case time :: score :: event :: _ => Some((time, score, event))
        // time - score - url and names -
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
