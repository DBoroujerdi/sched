package github.dboroujerdi.sched.sse

import akka.stream.scaladsl.{Flow, Sink, Source}
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.model.Types.Schedule
import spray.json._

trait PublisherComponent extends Protocols {
  self: ActorSystemComponent =>

  val jobManagerSource = Source.actorPublisher[Schedule](ScheduleSource.props)
  val ref = Flow[Schedule]
    .map(_.toJson)
    .map(println)
    .to(Sink.ignore)
    .runWith(jobManagerSource)

  object Publisher {
    def publish(events: Seq[ScheduledEvent]) = {
      ref ! events
    }
  }

  val streamPublisher = Publisher
}