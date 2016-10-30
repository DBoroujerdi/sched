package github.dboroujerdi.sched.sse

import akka.actor.{Actor, Props}
import akka.stream.actor.ActorPublisher
import github.dboroujerdi.sched.model.Types.Schedule

object ScheduleSource {
  def props: Props = Props[ScheduleSource]
}

class ScheduleSource extends Actor with ActorPublisher[Schedule] {
  import akka.stream.actor.ActorPublisherMessage._
  var items: List[Schedule] = List.empty

  def receive = {
    case s: Schedule =>
      if (totalDemand == 0)
        items = items :+ s
      else
        onNext(s)

    case Request(demand) =>
      if (demand > items.size){
        items foreach (onNext)
        items = List.empty
      }
      else {
        val (send, keep) = items.splitAt(demand.toInt)
        items = keep
        send foreach (onNext)
      }


    case other =>
      println(s"got other $other")
  }}