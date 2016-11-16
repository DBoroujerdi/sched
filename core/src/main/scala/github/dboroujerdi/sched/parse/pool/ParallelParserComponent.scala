package github.dboroujerdi.sched.parse.pool

import akka.actor.{Actor, ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern._
import akka.routing.RoundRobinPool
import akka.util.Timeout
import cats.data.OptionT
import github.dboroujerdi.sched.FutureMaybe
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse.Types.ErrorOrEvent
import github.dboroujerdi.sched.parse.pool.ParserMaster.{CompletedWork, Start}
import github.dboroujerdi.sched.parse.pool.WorkerParser.Work
import github.dboroujerdi.sched.parse.{HtmlScheduleParser, MatchElementParser, ParserComponent, TimeParser}
import net.ruippeixotog.scalascraper.model.{Document, Element}

import scala.concurrent.duration._

trait ParallelParserComponent extends ParserComponent {
  this: ActorSystemComponent =>

  private val htmlParser = new HtmlScheduleParser(TimeParser)

  implicit val timeout: Timeout = 5 seconds

  class ParallelParser(implicit system: ActorSystem) extends Parser {
    def parse(doc: Document): FutureMaybe[Schedule] = {
      val elements = htmlParser.parseMatchElements(doc)
      val masterRef = system.actorOf(Props(new ParserMaster(elements)))

      OptionT {
        masterRef
          .ask(Start)
          .mapTo[Seq[ErrorOrEvent]]
          .map(x => logAndFilterFailures(x))
      }
    }
  }

  override val parser: Parser = new ParallelParser
}

private[pool] object ParserMaster {

  object Start

  case class ParseWork(elems: Seq[Element])

  case class CompletedWork(event: ErrorOrEvent)

}

// divies out the work to worker actors and aggregates the result
private[pool] class ParserMaster(val elements: Seq[Element]) extends Actor {

  // todo send self a message to just return work done so far in-case not all works return in time

  val workRouter: ActorRef = context.actorOf(
    RoundRobinPool(8).props(Props[WorkerParser]),
    "parse-work-router"
  )

  def receive: Receive = waitingToStart

  def working(completed: List[ErrorOrEvent], recipient: ActorRef): Receive = {
    case CompletedWork(event) =>
      if (completed.length == elements.length - 1) {
        recipient ! completed
        self ! PoisonPill
      } else {
        context.become(working(event :: completed, recipient))
      }
  }

  def waitingToStart: Receive = {
    case Start =>
      elements.foreach(elem => workRouter ! Work(elem))
      context.become(working(List(), sender()))
  }
}

object WorkerParser {

  case class Work(elem: Element)

}

private[pool] class WorkerParser extends Actor {

  val parser = new MatchElementParser(TimeParser)

  override def receive: Receive = {
    case Work(elem) =>
      sender ! CompletedWork(parser.parseElement(elem))
  }
}