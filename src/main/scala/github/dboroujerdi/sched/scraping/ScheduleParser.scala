package github.dboroujerdi.sched.scraping

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr => _, element => _, elementList => _, text => _}

object ScheduleParser {

  object UrlExtractor {
    def unapply(in: java.net.URL) = Some((
      in.getProtocol,
      in.getHost,
      in.getPort,
      in.getPath
      ))
  }

  def parseRows(doc: Document) = {
    val tables = for {
      tables <- (doc >?> elementList(".tableData")).toSeq
      table <- tables
      rows <- (table >?> elementList("tr")).toSeq
    } yield rows.tail

    tables.flatten.map(_ >> elementList("td"))
      .flatMap(toTuple)
      .map({
        case (time, game, sport) =>
          (time >?> text("span"),
            game >?> (attr("href")("a"), text("a")),
            sport >?> text("a"))
      })
      .collect({
        case (Some(time), (Some(link), Some(name)), Some(sport)) =>
          (name.toString, time.toString, link.toString, sport.toString)
      })
  }

  private def toTuple(l: List[Element]) = l match {
    case f :: s :: t :: Nil => Some((f, s, t))
    case _else => None
  }
}
