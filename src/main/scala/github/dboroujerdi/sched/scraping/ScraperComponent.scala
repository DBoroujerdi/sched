package github.dboroujerdi.sched.scraping

import github.dboroujerdi.sched.model.Types.Schedule
import github.dboroujerdi.sched.parse.ParserComponent

trait ScraperComponent {

  trait Scraper {
    def scrape(url: String): Schedule
  }

  val scraper: Scraper
}

trait WebScraperComponent extends ScraperComponent {
  this: BrowserComponent with ParserComponent =>

  object WebScraper extends Scraper {
    override def scrape(url: String): Schedule = {
      val doc = browser.fetchDocument(url)
      parser.parse(doc)
    }
  }

  override val scraper: Scraper = WebScraper
}

