package github.dboroujerdi.sched.sse

import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.model.Types.Schedule
import org.joda.time.DateTime
import org.joda.time.format.{DateTimeFormatter, ISODateTimeFormat}
import spray.json.DefaultJsonProtocol
import spray.json._

trait Protocols extends DefaultJsonProtocol {

  implicit object DateJsonFormat extends RootJsonFormat[DateTime] {
    private val parserISO : DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();
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
