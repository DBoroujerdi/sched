package github.dboroujerdi.sched.scraping

import cats.data.Xor
import github.dboroujerdi.sched.model.ScheduledEvent
import github.dboroujerdi.sched.scraping.Types.ErrorOrEvent

trait ScraperComponent {

  trait Scraper {
    def scrape(url: String): Seq[ScheduledEvent]
  }

  val scraper: Scraper
}

trait WebScraperComponent extends ScraperComponent {
  this: BrowserComponent =>

  val htmlScheduleParser = new HtmlScheduleParser(TimeParser)

  object WebScraper extends Scraper {
    override def scrape(url: String): Seq[ScheduledEvent] = {
      val doc = browser.fetchDocument(url)
      val events = htmlScheduleParser.parseSchedule(doc)

      logAndFilterFailures(events)
    }

    private def logAndFilterFailures(list: Seq[ErrorOrEvent]) = {
      list.filter(_.isLeft).foreach {
        case Xor.Left(error @ ExceptionalScrapeError(_, _)) => println("Unable to parse: ", error)
        case _ =>
      }

      list.collect {
        case Xor.Right(event) => event
      }
    }
  }

  override val scraper: Scraper = WebScraper
}

