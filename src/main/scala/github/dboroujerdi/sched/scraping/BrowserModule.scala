package github.dboroujerdi.sched.scraping

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

trait BrowserModule {

  trait Browser {
    type DocumentFetcher = String => Document

    protected val browser = JsoupBrowser()
    val fetchDocument: DocumentFetcher
  }

  val browser: Browser
}

trait WebBrowserModule extends BrowserModule {

  class WebBrowser extends Browser {
    override val fetchDocument: DocumentFetcher = browser.get
  }

  val browser: Browser = new WebBrowser()
}
