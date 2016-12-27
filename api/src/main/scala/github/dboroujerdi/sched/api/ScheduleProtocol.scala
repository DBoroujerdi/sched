package github.dboroujerdi.sched.api

import github.dboroujerdi.sched.api.model.{InPlayEvent, PreMatchEvent, ScheduledEvent, Score}
import github.dboroujerdi.sched.api.model.Types.Schedule
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json.{DefaultJsonProtocol, _}

trait ScheduleProtocol extends DefaultJsonProtocol {

  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    private val parserISO = ISODateTimeFormat.dateTimeNoMillis()
    override def write(obj: DateTime) = JsString(parserISO.print(obj))
    override def read(json: JsValue) : DateTime = ???
  }

  implicit val scoreFormat: RootJsonFormat[Score] = jsonFormat2(Score.apply)

  implicit val preMatchEventFormat: RootJsonFormat[PreMatchEvent] = jsonFormat5(PreMatchEvent)
  implicit val inPlayEventFormat: RootJsonFormat[InPlayEvent] = jsonFormat6(InPlayEvent)

  implicit object ScheduledEvent extends RootJsonFormat[ScheduledEvent] {
    override def write(obj: ScheduledEvent): JsValue = obj match {
      case event: PreMatchEvent => event.toJson
      case event: InPlayEvent => event.toJson
    }
    override def read(json: JsValue): ScheduledEvent = ???
  }

  implicit object ScheduleFormat extends RootJsonFormat[Seq[ScheduledEvent]] {
    override def write(obj: Schedule): JsValue = JsArray(obj.map(_.toJson).toVector)
    override def read(json: JsValue): Schedule = ???
  }
}

object ScheduleProtocol extends ScheduleProtocol
