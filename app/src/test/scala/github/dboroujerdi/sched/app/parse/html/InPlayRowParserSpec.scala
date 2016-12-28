package github.dboroujerdi.sched.app.parse.html

import cats.data.Xor
import github.dboroujerdi.sched.api.model.{InPlayEvent, Score}
import org.scalatest.{FunSpec, Matchers}

class InPlayRowParserSpec extends FunSpec
  with Matchers {

  val matchElement :: _ = PageParser.parseElementRows(TestData.exampleInPlaySchedule)

  describe("RowParser") {

    it("should parse in play row element in domain model") {
      val res = InPlay.parse(matchElement)

      res should matchPattern {
        case Xor.Right(InPlayEvent("12345", "Manchester United", "Arsenal", "Football", "80:00", Score(0, 4))) =>
      }
    }
  }
}
