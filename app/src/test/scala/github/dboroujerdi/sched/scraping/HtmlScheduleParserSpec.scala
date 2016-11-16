package github.dboroujerdi.sched.scraping

import cats.data.Xor
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.parse.{HtmlScheduleParser, NoParseError, TimeParser}
import github.dboroujerdi.sched.scraping.TestData.exampleSchedule
import org.joda.time.DateTime
import org.scalatest.{FunSpec, Matchers}

class HtmlScheduleParserSpec extends FunSpec with Matchers {

  object FakeTimeParser extends TimeParser {
    override def parseTime(rawTime: String, currentDate: DateTime): Option[DateTime] = Some(DateTime.now())
  }

  describe("Schedule HTML parsing") {
    describe("given test schedule") {
      val htmlParser = new HtmlScheduleParser(FakeTimeParser)

      it("should parse only the football matches") {
        val parsed = htmlParser.parseSchedule(exampleSchedule)
        assert(parsed.length == 6)

        val failed = parsed.filter(_.isLeft)
        assert(failed.length == 1)

        val Xor.Left(singleFailed) = failed.head
        singleFailed shouldBe a [NoParseError]

        val events = parsed.filter(_.isRight)
        assert(events.length == 5)

        events should matchPattern {
          case List(
            Xor.Right(ScheduledEvent("123456", "Man City", "West Ham", _, "Football")),
            Xor.Right(ScheduledEvent("123456", "Man City", "West Ham", _, "Football")),
            Xor.Right(ScheduledEvent("10010467", "Arsenal", "Man Utd", _, "Football")),
            Xor.Right(ScheduledEvent("10010467", "Bulls", "Eagles", _, "Basketball")),
            Xor.Right(ScheduledEvent("10010467", "Watford", "Chelsea", _, "Football"))
          ) =>
        }
      }
    }
  }
}

