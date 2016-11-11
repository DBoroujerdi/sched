package github.dboroujerdi.sched.parse

import github.dboroujerdi.sched.parse.Types._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr => _, element => _, elementList => _, text => _}

class HtmlScheduleParser(timeParser: TimeParser) {

  private val matchElementParser = new MatchElementParser(timeParser)

  private type TeamsNames = (String, String)

  def parseSchedule(doc: Document): Seq[ErrorOrEvent] = {
    parseElements(pullOutEventElements(doc))
  }

  private def parseElements(elements: Seq[Element]): Seq[ErrorOrEvent] = {
    for {element <- elements} yield matchElementParser.parseElement(element)
  }

  private def pullOutEventElements(doc: Document): Seq[Element] = {
    (for {
      tables <- (doc >?> elementList(".tableData")).toSeq
      table <- tables
      rows <- (table >?> elementList("tr")).toSeq
    } yield rows.tail).flatten
  }
}
