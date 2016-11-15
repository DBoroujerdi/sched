package github.dboroujerdi.sched.parse.stream

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.Timeout
import cats.data.{OptionT, Xor}
import github.dboroujerdi.sched.FutureMaybe
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse._
import net.ruippeixotog.scalascraper.model.{Document, Element}

import scala.concurrent.Future
import scala.concurrent.duration._

trait StreamParseComponent extends ParserComponent {
  this: ActorSystemComponent =>

  class StreamParser extends Parser {

    private val htmlParser = new HtmlScheduleParser(TimeParser)
    val parser = new MatchElementParser(TimeParser)

    implicit val timeout: Timeout = 5 seconds

    val parserFlow =
      Flow[Element]
        .map(parser.parseElement)
        .filter {
          case Xor.Left(error@ExceptionalScrapeError(_, _)) => false
          case _ => true
        }
        .collect {
          case Xor.Right(event) => event
        }

    def parse(doc: Document): FutureMaybe[Schedule] = {
      val elements: Seq[Element] = htmlParser.parseMatchElements(doc)

      OptionT {
        Source(elements.toList)
          .via(parserFlow)
          .toMat(Sink.seq[ScheduledEvent])(Keep.right)
          .run()
          .map(Option(_))
      }
    }
  }

  val parser: Parser = new StreamParser
}
