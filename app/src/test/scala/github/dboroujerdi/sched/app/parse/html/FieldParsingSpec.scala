package github.dboroujerdi.sched.app.parse.html

import org.scalatest.{FunSpec, Matchers}

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

    describe("Score string parsing") {

      it("should parse a score string in format 'n-m'") {
        parseScore("0-0") should matchPattern {
          case Some((0, 0)) =>
        }

        parseScore("2-1") should matchPattern {
          case Some((2, 1)) =>
        }
      }

      it("should handle scores of the correct format but without numbers") {
        parseScore("NaN-1") should matchPattern {
          case None =>
        }
      }

      it("should handle whitespace in the string") {
        parseScore(" 1 -1 ") should matchPattern {
          case Some((1, 1)) =>
        }
      }

      it("should parse scores in the double digits") {
        parseScore("11-0") should matchPattern {
          case Some((11, 0)) =>
        }
      }
    }
  }
}
