package github.dboroujerdi.sched.sse

import akka.stream.actor.ActorPublisher
import akka.stream.scaladsl.{Sink, Source}
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.model.Types.Schedule
import org.reactivestreams.Publisher

import spray.json._

trait PublisherComponent {
  val publisher: Publisher[String]
}

trait StreamPublisherComponent extends PublisherComponent with Protocols {
  self: ActorSystemComponent =>

  private val publisherRef = system.actorOf(SchedulePublisher.props, "publisher-actor")

  def publish(schedule: Schedule): Unit = {
    println("publishing")
    publisherRef ! Publish(schedule)
  }

  val publisher = Source.fromPublisher(ActorPublisher[Schedule](publisherRef))
    .map(_.toJson.toString)
    .runWith(Sink.asPublisher(fanout = true))
}