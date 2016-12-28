package github.dboroujerdi.sched.app.parse.html

import org.scalatest.{FunSpec, Matchers}

class PageParserSpec extends FunSpec
  with Matchers {

  describe("Schedule HTML parsing") {

    describe("Pre-match schedule") {

      it("should parse match rows") {
        val parsed = PageParser.parseElementRows(TestData.examplePreMatchSchedule)
        assert(parsed.length == 8)
      }
    }

    describe("In-play schedule") {

      it("should parse match rows") {
        val parsed = PageParser.parseElementRows(TestData.exampleInPlaySchedule)
        assert(parsed.length == 3)
      }
    }
  }
}

