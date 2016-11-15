package github.dboroujerdi.sched.server

import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.model.Types.Schedule
import org.reactivestreams.Publisher

trait PublisherComponent {
  val publisher: Publisher[Schedule]
}

trait StreamPublisherComponent extends PublisherComponent {
  self: ActorSystemComponent =>

  private val publisherRef = system.actorOf(SchedulePublisher.props, "publisher-actor")

  def publish(schedule: Schedule): Unit = {
    println("publishing")
    publisherRef ! Publish(schedule)
  }

  val publisher =
    Source
    .fromPublisher(ActorPublisher[Schedule](publisherRef))
    .runWith(Sink.asPublisher(fanout = true))
}