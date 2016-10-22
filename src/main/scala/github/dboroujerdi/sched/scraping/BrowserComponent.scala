package github.dboroujerdi.sched.scraping

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

trait BrowserComponent {

  trait Browser {
    final val browser = JsoupBrowser()
    def fetchDocument(location: String): Document
  }

  val browser: Browser
}

trait WebBrowserComponent extends BrowserComponent {

  class WebBrowser extends Browser {
    override def fetchDocument(location: String): Document = {
      browser.get(location)
    }
  }

  val browser: Browser = new WebBrowser()
}
