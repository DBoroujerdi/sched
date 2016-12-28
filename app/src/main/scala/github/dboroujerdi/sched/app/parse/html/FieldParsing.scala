package github.dboroujerdi.sched.app.parse.html

import java.net.URL

import github.dboroujerdi.sched.api.model.Types.MatchId
import github.dboroujerdi.sched.app.parse.TeamsNames
import github.dboroujerdi.sched.app.parse.util._

import scala.util.{Failure, Success, Try}

// todo unit testing
trait FieldParsing {

  def parseScore(score: String): Option[(Int, Int)] = {
    score.clean().removeWhitespace().split("-") match {
      case Array(home, away) => Try((home.toInt, away.toInt)).toOption
      case _ => None
    }
  }

  def parseTeamNames(name: String): Option[TeamsNames] = {
    name.clean().split(" v ") match {
      case Array(home, away) => Some((home.trim, away.trim))
      case _ => None
    }
  }

  def parseIdFromUrl(urlS: String): Option[MatchId] = {
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
}

object FieldParsing extends FieldParsing
