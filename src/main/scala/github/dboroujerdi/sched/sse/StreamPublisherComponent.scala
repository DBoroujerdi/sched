package github.dboroujerdi.sched.sse

import akka.http.scaladsl.model.HttpEntity.ChunkStreamPart
import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}
import github.dboroujerdi.sched.model.ScheduledEvent
import org.reactivestreams.Publisher

trait PublisherComponent {
  val publisher: Publisher[ChunkStreamPart]
}

trait StreamPublisherComponent extends PublisherComponent {
  self: ActorSystemComponent =>

  private val publisherRef = system.actorOf(SchedulePublisher.props, "publisher-actor")

  def publish(schedule: Seq[ScheduledEvent]): Unit = {
    publisherRef ! schedule
  }

  val publisher = Source.fromPublisher(ActorPublisher[ChunkStreamPart](publisherRef)).runWith(Sink.asPublisher(fanout = true))
}