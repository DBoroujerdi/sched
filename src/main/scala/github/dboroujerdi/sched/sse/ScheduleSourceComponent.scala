package github.dboroujerdi.sched.sse

import akka.actor.ActorRef
import akka.stream.scaladsl.{Flow, Source}
import de.heikoseeberger.akkasse.ServerSentEvent
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.model.Types.Schedule
import spray.json.pimpAny

import scala.concurrent.duration.DurationInt

trait ScheduleSourceComponent extends Protocols {
  self: ActorSystemComponent =>

  val source: Source[Schedule, ActorRef] = Source.actorPublisher[Schedule](SchedulePublisher.props)

  val flow = Flow[Schedule]
    .map(event => event.toJson.toString)
    .map(json => ServerSentEvent(json, "schedule"))
    .keepAlive(1.second, () => ServerSentEvent.Heartbeat)
}