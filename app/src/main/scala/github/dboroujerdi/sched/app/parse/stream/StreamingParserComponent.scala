package github.dboroujerdi.sched.app.parse.stream

import akka.NotUsed
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import akka.util.Timeout
import cats.data.{OptionT, Xor}
import github.dboroujerdi.sched.api.model.ScheduledEvent
import github.dboroujerdi.sched.api.model.Types.Schedule
import github.dboroujerdi.sched.app.FutureMaybe
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.app.parse._
import github.dboroujerdi.sched.app.parse.html.Types.ErrorOrEvent
import github.dboroujerdi.sched.app.parse.html._
import github.dboroujerdi.sched.app.parse.html.inplay.InPlayRowParser
import github.dboroujerdi.sched.app.parse.html.prematch.{EventElementParser, RawTimeParser}
import net.ruippeixotog.scalascraper.model.{Document, Element}

import scala.concurrent.duration._

trait StreamingParserComponent extends ParserComponent {
  this: ActorSystemComponent =>

  class StreamParsers extends Parsers {

    type ElementParser = Element => ErrorOrEvent

    private val eventParser = new EventElementParser with RawTimeParser

    implicit val timeout: Timeout = 5 seconds

    private def parserFlow(elementParser: ElementParser): Flow[Element, ScheduledEvent, NotUsed] =
      Flow[Element]
        .map(elementParser)
        .filter {
          case Xor.Left(error@ExceptionalScrapeError(_, _)) => false
          case _ => true
        }
        .collect {
          case Xor.Right(event) => event
        }

    def parse(flow: Flow[Element, ScheduledEvent, NotUsed])(doc: Document): FutureMaybe[Schedule] = {
      val elements: Seq[Element] = PageParser.parseElementRows(doc)

      OptionT {
        Source(elements.toList)
          .via(flow)
          .toMat(Sink.seq[ScheduledEvent])(Keep.right)
          .run()
          .map(Option(_))
      }
    }

    val inPlayParser: Parser = document => {
      val flow = parserFlow(InPlayRowParser.parseRow)
      parse(flow)(document)
    }
    val preMatchParser: Parser = document => {
      val flow = parserFlow(eventParser.parseElement)
      parse(flow)(document)
    }
  }

  val parsers: Parsers = new StreamParsers
}
