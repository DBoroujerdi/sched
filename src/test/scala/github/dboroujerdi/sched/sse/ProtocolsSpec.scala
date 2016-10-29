package github.dboroujerdi.sched.sse

import github.dboroujerdi.sched.model.ScheduledEvent
import org.joda.time.DateTime
import org.scalatest.FunSpec
import spray.json._

class ProtocolsSpec extends FunSpec with Protocols {

  describe("Json Protocol") {
    it("should encode a ScheduledEvent as JSON") {
      val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22).withMillis(0)
      val event = ScheduledEvent("1234", "Arsenal", "Chelsea", fakeCurrentDate, "Football")

      assert(event.toJson.toString() ==
        "{\"home\":\"Arsenal\",\"sport\":\"Football\",\"id\":\"1234\",\"time\":\"1970-01-01T01:00:00+01:00\",\"away\":\"Chelsea\"}")
    }
  }
}
