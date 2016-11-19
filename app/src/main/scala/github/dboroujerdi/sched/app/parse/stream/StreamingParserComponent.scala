package github.dboroujerdi.sched.app.parse.stream

import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.Timeout
import cats.data.{OptionT, Xor}
import github.dboroujerdi.sched.api.model.ScheduledEvent
import github.dboroujerdi.sched.api.model.Types.Schedule
import github.dboroujerdi.sched.app.FutureMaybe
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.app.parse._
import github.dboroujerdi.sched.app.parse.html._
import net.ruippeixotog.scalascraper.model.{Document, Element}

import scala.concurrent.duration._

trait StreamingParserComponent extends ParserComponent {
  this: ActorSystemComponent =>

  class StreamParser extends Parser {

    private val eventParser = new EventElementParser with RawTimeParser

    implicit val timeout: Timeout = 5 seconds

    val parserFlow =
      Flow[Element]
        .map(eventParser.parseElement)
        .filter {
          case Xor.Left(error@ExceptionalScrapeError(_, _)) => false
          case _ => true
        }
        .collect {
          case Xor.Right(event) => event
        }

    def parse(doc: Document): FutureMaybe[Schedule] = {
      val elements: Seq[Element] = PageParser.parseMatchElements(doc)

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
