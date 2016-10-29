package github.dboroujerdi.sched.scraping

import org.joda.time.DateTime
import org.scalatest.FunSpec

class TimeParserSpec extends FunSpec {

  describe("TimerParser") {

    val fakeCurrentDate = DateTime.now().withDate(2016, 10, 22)

    it("should parse \"Live At23:30 UK\"") {
      val time = TimeParser.parseTime("Live At23:30 UK ", fakeCurrentDate)
      assert(time.isDefined)
      assert(isToday(time.get, fakeCurrentDate))
    }

    it("should parse \"23 Oct15:00 UK\"") {
      val time = TimeParser.parseTime("23 Oct15:00 UK", fakeCurrentDate)
      assert(time.isDefined)

      val dateTime = time.get
      assert(dateTime.getYear == 2016)
      assert(dateTime.getMonthOfYear == 10)
      assert(dateTime.getDayOfMonth == 23)
    }

    it("should parse \"2 Jan15:00 UK\" as next year") {
      val time = TimeParser.parseTime("2 Jan15:00 UK", fakeCurrentDate)
      assert(time.isDefined)

      val dateTime = time.get
      assert(dateTime.getYear == 2017)
      assert(dateTime.getMonthOfYear == 1)
      assert(dateTime.getDayOfMonth == 2)
    }

    it("should not parse the time of a match that has already started") {
      val time = TimeParser.parseTime("Event Started!", fakeCurrentDate)
      assert(time.isEmpty)
    }
  }

  def isToday(time: DateTime, today: DateTime): Boolean = {
    (today.year() == time.year()) &&
      (today.monthOfYear() == time.monthOfYear()) &&
      (today.dayOfMonth() == time.dayOfMonth())
  }
}
