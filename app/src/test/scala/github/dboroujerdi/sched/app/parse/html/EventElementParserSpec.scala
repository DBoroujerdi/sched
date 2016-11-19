package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.api.model.ScheduledEvent
import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}

class EventElementParserSpec extends FunSpec
  with Matchers {

  val matchElement :: _ = PageParser.parseMatchElements(TestData.exampleSchedule)

  describe("Event element parser") {
    val eventElementParser = new EventElementParser with TimeParser {
      def parseTime(rawTime: String, currentDate: DateTime): Option[DateTime] = Some(DateTime.now())
    }

    describe("given match element") {
      it("should parse event html element") {
        val scheduleEvent = eventElementParser.parseElement(matchElement)
        scheduleEvent should matchPattern { case Xor.Right(ScheduledEvent("123456", "Man City", "West Ham", _, "Football")) => }
      }
    }
  }
}