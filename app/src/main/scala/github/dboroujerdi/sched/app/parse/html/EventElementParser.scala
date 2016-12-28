package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import github.dboroujerdi.sched.app.parse.{FieldExtractor, FieldParser}
import net.ruippeixotog.scalascraper.model.Element

object EventElementParser {

  def parseRow(parseField: FieldParser)(extractFields: FieldExtractor)(rowElement: Element): ErrorOrEvent = {
    try {
      extractFields(rowElement)
        .flatMap(parseField(_)) match {
        case Some(event) => Xor.Right(event)
        case None => Xor.Left(NoParseError(rowElement))
      }
    } catch {
      case ex: Exception =>
        Xor.Left(ExceptionalScrapeError(rowElement, ex))
    }
  }
}
