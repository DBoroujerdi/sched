package github.dboroujerdi.sched.app.parse.html

import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.{Document, Element}

object PageParser {

  def parseElementRows(doc: Document): Seq[Element] = {
    (for {
      tables <- (doc >?> elementList(".tableData")).toSeq
      table <- tables
      rows <- (table >?> elementList("tr")).toSeq
    } yield rows).flatten
  }
}
