package github.dboroujerdi.sched.web

import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.model.Types.Schedule
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import spray.json.{DefaultJsonProtocol, _}

trait Protocols extends DefaultJsonProtocol {

  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    private val parserISO = ISODateTimeFormat.dateTimeNoMillis()
    override def write(obj: DateTime) = JsString(parserISO.print(obj))
    override def read(json: JsValue) : DateTime = ???
  }

  implicit object ScheduleFormat extends RootJsonFormat[Seq[ScheduledEvent]] {
    override def write(obj: Schedule): JsValue = JsArray(obj.map(_.toJson).toVector)
    override def read(json: JsValue): Schedule = ???
  }

  implicit val ScheduledEventFormat = jsonFormat5(ScheduledEvent)
}

object Protocols extends Protocols
