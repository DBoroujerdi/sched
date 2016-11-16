package github.dboroujerdi.sched.server

import github.dboroujerdi.sched.api.ScheduleProtocol
import github.dboroujerdi.sched.api.model.ScheduledEvent
import github.dboroujerdi.sched.api.model.Types.Schedule
import org.joda.time.DateTime
import org.scalatest.FunSpec
import spray.json._

class ScheduleProtocolsSpec extends FunSpec
  with ScheduleProtocol {

  describe("Json Protocol") {
    it("should encode a ScheduledEvent as JSON") {
      val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22).withMillis(0)
      val event = ScheduledEvent("1234", "Arsenal", "Chelsea", fakeCurrentDate, "Football")

      assert(event.toJson.toString ==
        "{\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Chelsea\"}")
    }

    it("should encode a sequence of ScheduledEvents as JSON") {
      val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22).withMillis(0)
      val event1 = ScheduledEvent("1234", "Arsenal", "Chelsea", fakeCurrentDate, "Football")
      val event2 = ScheduledEvent("4321", "Man Utd", "Watford", fakeCurrentDate, "Football")

      val events: Schedule = Seq(event1, event2)
      assert(events.toJson.toString ==
        "[{\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Chelsea\"},{\"home\":\"Man Utd\",\"sport\":\"Football\",\"id\":\"4321\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Watford\"}]")
    }
  }
}
