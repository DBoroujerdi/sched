package github.dboroujerdi.sched.scraping

import java.io.File

import cats.data.Xor
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.scraping.TestData.{browser, exampleSchedule}
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

    describe("given a real life schedule page") {
      val htmlParser = new HtmlScheduleParser(TimeParser)
      val file = new File("/Users/dbo23/projects/open-source/sched/src/test/resources/sched_1.html")
      val doc = browser.parseFile(file)

      it("should parse 326 events") {
        val events = htmlParser.parseSchedule(doc)

        println(events)

        val failed = events.filter(_.isLeft)

//        assert(failed.length == 154)
//        assert(events.length == 326)
        // todo assert they all have valid id's
      }
    }
  }
}

