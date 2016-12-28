package github.dboroujerdi.sched.app.parse.util

import java.net.URL

import org.scalatest.FunSpec

class UrlParseUtilsSpec extends FunSpec {

  import github.dboroujerdi.sched.app.parse.util._

  describe("Url parsing") {
    describe("given a url") {
      val url = new URL("http://google.co.uk/this/is/a/path")
      it("return extract path") {
        val res = url.extractPath

        assert(res.isDefined)
        assert(res.contains("/this/is/a/path"))
      }
    }

    describe("given a url without a path") {
      val url = new URL("http://google.co.uk")
      it("should return a None type") {
        val res = url.extractPath

        assert(res.isEmpty)
      }
    }
  }
}
