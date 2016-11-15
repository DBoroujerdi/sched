package github.dboroujerdi.sched.server

import akka.actor.{Actor, Props}
import akka.stream.actor.ActorPublisher
import github.dboroujerdi.sched.model.Types.Schedule

object SchedulePublisher {
  def props: Props = Props[SchedulePublisher]
}

case class Publish(schedule: Schedule)

class SchedulePublisher extends Actor
  with ActorPublisher[Schedule]
  with Protocols {

  import akka.stream.actor.ActorPublisherMessage._

  def receive: Receive = empty

  def publishing(currentSchedule: Schedule): Receive = {
    case Publish(schedule) =>
      if (totalDemand > 0) {
        onNext(schedule)
        context.become(empty)
      } else {
        context.become(publishing(schedule))
      }
    case Request(_) =>
      onNext(currentSchedule)
      context.become(empty)
  }

  def empty: Receive = {
    case Publish(schedule) =>
      context.become(publishing(schedule))
    case Request(demand) =>
  }
}