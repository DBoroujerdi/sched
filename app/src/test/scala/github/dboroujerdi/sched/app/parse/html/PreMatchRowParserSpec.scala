package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.api.model.PreMatchEvent
import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}

class PreMatchRowParserSpec extends FunSpec
  with Matchers {

  val matchElement :: _ = PageParser.parseElementRows(TestData.examplePreMatchSchedule).tail

  describe("Event element parser") {
    val timeParser = new TimeParser {
      def parseTime(rawTime: String, currentDate: DateTime): Option[DateTime] = Some(DateTime.now())
    }

    describe("given match element") {
      it("should parse event html element") {
        PreMatch.parser(timeParser)(matchElement) should matchPattern {
          case Xor.Right(PreMatchEvent("123456", "Man City", "West Ham", "Football", _)) =>
        }
      }
    }
  }
}