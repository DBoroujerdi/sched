package github.dboroujerdi.sched.app.parse.html

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}
import net.ruippeixotog.scalascraper.scraper.ContentExtractors.{attr => _, element => _, elementList => _, text => _}

object PageParser {

  private type TeamsNames = (String, String)

  def parseMatchElements(doc: Document): Seq[Element] = {
    (for {
      tables <- (doc >?> elementList(".tableData")).toSeq
      table <- tables
      rows <- (table >?> elementList("tr")).toSeq
    } yield rows.tail).flatten
  }
}
