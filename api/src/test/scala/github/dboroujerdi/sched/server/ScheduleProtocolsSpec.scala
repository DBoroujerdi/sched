package github.dboroujerdi.sched.server

import github.dboroujerdi.sched.api.ScheduleProtocol
import github.dboroujerdi.sched.api.model.{InPlayEvent, PreMatchEvent, ScheduledEvent, Score}
import github.dboroujerdi.sched.api.model.Types.Schedule
import org.joda.time.DateTime
import org.scalatest.FunSpec
import spray.json._

class ScheduleProtocolsSpec extends FunSpec
  with ScheduleProtocol {

  describe("Json Protocol") {

    describe("PreMatchEvent protocol") {
      it("should encode PreMatchEvent") {
        val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22).withMillis(0)
        val event = PreMatchEvent("1234", "Arsenal", "Chelsea", "Football", fakeCurrentDate)

        assert(event.toJson.toString ==
          "{\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Chelsea\"}")
      }

      it("should encode a sequence of PreMatchEvents") {
        val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22).withMillis(0)
        val event1 = PreMatchEvent("1234", "Arsenal", "Chelsea", "Football", fakeCurrentDate)
        val event2 = PreMatchEvent("4321", "Man Utd", "Watford", "Football", fakeCurrentDate)

        val events: Schedule = Seq(event1, event2)
        assert(events.toJson.toString ==
          "[{\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Chelsea\"},{\"home\":\"Man Utd\",\"sport\":\"Football\",\"id\":\"4321\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Watford\"}]")
      }
    }

    describe("InPlayEvent protocol") {
      it("should encode InPlayEvent") {
        val event = InPlayEvent("1234", "Arsenal", "Chelsea", "Football", "14:10", Score(1, 0))

        assert(event.toJson.toString ==
          "{\"score\":{\"home\":1,\"away\":0},\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"matchTime\":\"14:10\",\"away\":\"Chelsea\"}")
      }

      it("should encode a sequence of InPlayEvents") {
        val event1 = InPlayEvent("1234", "Arsenal", "Chelsea", "Football", "14:10", Score(1, 0))
        val event2 = InPlayEvent("4321", "Tottenham", "Watford", "Football", "05:10", Score(0, 1))

        assert(Seq(event1, event2).toJson.toString ==
          "[{\"score\":{\"home\":1,\"away\":0},\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"matchTime\":\"14:10\",\"away\":\"Chelsea\"},{\"score\":{\"home\":0,\"away\":1},\"home\":\"Tottenham\",\"sport\":\"Football\",\"id\":\"4321\",\"matchTime\":\"05:10\",\"away\":\"Watford\"}]")
      }
    }
  }
}
