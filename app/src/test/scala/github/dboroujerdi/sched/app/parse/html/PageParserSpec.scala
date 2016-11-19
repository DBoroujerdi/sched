package github.dboroujerdi.sched.app.parse.html

import org.scalatest.{FunSpec, Matchers}

class PageParserSpec extends FunSpec
  with Matchers {

  describe("Schedule HTML parsing") {

    describe("given test schedule") {

      it("should parse only the football matches") {
        val parsed = PageParser.parseMatchElements(TestData.exampleSchedule)
        assert(parsed.length == 6)
      }
    }
  }
}

