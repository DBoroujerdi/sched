package github.dboroujerdi.sched.app.parse.html

import org.scalatest.{FunSpec, FunSuite, Matchers}

class FieldParsingSpec extends FunSpec
  with Matchers
  with FieldParsing {

  describe("Field Parsing") {

    describe("parsing team names from a string 'x v y'") {

      it("should extract team names") {
        parseTeamNames("Arsenal v Manchester United") should matchPattern {
          case Some(("Arsenal", "Manchester United")) =>
        }
      }

      it("should not parse team names where the string does not follow the format 'x v y'") {
        parseTeamNames("Arsenal") should matchPattern {
          case None =>
        }
      }

      it("should handle unicode space characters the name") {
        parseTeamNames("Arsenal\u00A0 v \u00A0Manchester United") should matchPattern {
          case Some(("Arsenal", "Manchester United")) =>
        }
      }

      it("should trim any whitespace around team names") {
        parseTeamNames(" Arsenal v  Manchester United") should matchPattern {
          case Some(("Arsenal", "Manchester United")) =>
        }
      }
    }
  }
}
