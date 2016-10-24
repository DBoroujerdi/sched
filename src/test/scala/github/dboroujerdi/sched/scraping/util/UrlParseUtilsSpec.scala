package github.dboroujerdi.sched.scraping.util

import java.net.URL

import org.scalatest.FunSpec

class UrlParseUtilsSpec extends FunSpec {

  describe("Url parsing") {
    describe("given a url") {
      val url = new URL("http://google.co.uk/this/is/a/path")
      it("return extract path") {
        val res = UrlParseUtils.extractPath(url)

        assert(res.isDefined)
        assert(res.contains("/this/is/a/path"))
      }
    }

    describe("given a url without a path") {
      val url = new URL("http://google.co.uk")
      it("should return a None type") {
        val res = UrlParseUtils.extractPath(url)

        assert(res.isEmpty)
      }
    }
  }

  describe("Path parsing") {
    describe("given a path") {
      val path = "/this/is/a/path"
      it("should split path by '/' into a list of strings") {
        val res = UrlParseUtils.splitPath(path)

        assert(res.length == 4)
        assert(res == Vector("this", "is", "a", "path"))
      }
    }
  }
}
