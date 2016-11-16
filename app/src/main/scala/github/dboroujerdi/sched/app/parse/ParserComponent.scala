package github.dboroujerdi.sched.app.parse

import cats.data.{OptionT, Xor}
import github.dboroujerdi.sched.api.model.Types.Schedule
import github.dboroujerdi.sched.app.FutureMaybe
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.app.parse.Types.ErrorOrEvent
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.Future

trait ParserComponent {

  trait Parser {

    protected def logAndFilterFailures(list: Seq[ErrorOrEvent]): Option[Schedule] = {
      list.filter(_.isLeft).foreach {
        case Xor.Left(error@ExceptionalScrapeError(_, _)) => println("Unable to parse: ", error)
        case _ =>
      }

      Option {
        list.collect {
          case Xor.Right(event) => event
        }
      }
    }

    def parse(doc: Document): FutureMaybe[Schedule]
  }

  val parser: Parser
}

trait SynchronousParserComponent extends ParserComponent {
  this: ActorSystemComponent =>

  class SynchronousParser extends Parser {

    private[this] val htmlParser = new HtmlScheduleParser(TimeParser)

    def parse(doc: Document): FutureMaybe[Schedule] = {
      OptionT(Future(logAndFilterFailures(htmlParser.parseSchedule(doc))))
    }
  }

  val parser: Parser = new SynchronousParser
}
