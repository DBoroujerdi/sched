package github.dboroujerdi.sched.sse

import akka.actor.{Actor, Props}
import akka.http.javadsl.model.HttpEntity.ChunkStreamPart
import akka.http.scaladsl.model.HttpEntity.Chunk
import akka.stream.actor.ActorPublisher
import github.dboroujerdi.sched.model.Types.Schedule
import spray.json._

object SchedulePublisher {
  def props: Props = Props[SchedulePublisher]
}

class SchedulePublisher extends Actor
  with ActorPublisher[ChunkStreamPart]
  with Protocols {

  import akka.stream.actor.ActorPublisherMessage._
  var items: List[ChunkStreamPart] = List.empty

  def receive = {
    case s: Schedule =>
      val chunk = Chunk(s.toJson.toString())
      if (totalDemand == 0)
        items = items :+ chunk
      else
        onNext(chunk)

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