package github.dboroujerdi.sched.parse

import cats.data.Xor
import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse.Types.ErrorOrEvent
import net.ruippeixotog.scalascraper.model.Document

trait ParserComponent {

  trait Parser {

    protected def logAndFilterFailures(list: Seq[ErrorOrEvent]) = {
      list.filter(_.isLeft).foreach {
        case Xor.Left(error @ ExceptionalScrapeError(_, _)) => println("Unable to parse: ", error)
        case _ =>
      }

      list.collect {
        case Xor.Right(event) => event
      }
    }

    def parse(doc: Document): Schedule
  }

  val parser: Parser
}

trait SynchronousParserComponent extends ParserComponent {

  class SynchronousParser extends Parser {

    private[this] val htmlParser = new HtmlScheduleParser(TimeParser)

    def parse(doc: Document): Schedule = {
      logAndFilterFailures(htmlParser.parseSchedule(doc))
    }
  }

  val parser: Parser = new SynchronousParser
}
