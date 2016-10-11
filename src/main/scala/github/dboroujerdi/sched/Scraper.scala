package github.dboroujerdi.sched

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupElement
import net.ruippeixotog.scalascraper.model.Element

trait Scraper {

  import net.ruippeixotog.scalascraper.dsl.DSL._
  import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
  import net.ruippeixotog.scalascraper.dsl.DSL.Parse._

  private[this] val browser = JsoupBrowser()

  protected def scrape(url: String) = {
    val doc = browser.get(url)

    val e = (doc >> element(".tableData"))
      .map(_ >> elementList("tr"))
      .map(_.tail)  // drop the table headers
      .flatMap(_ >> elementList("td"))
      .flatMap(toTuple)
      .map({
        case (time, game, sport) =>
          (time >> element("span") >> text("span"),
            game >> (attr("href")("a"), text("a")),
            sport >> text("a"))
      })

     e
  }

  private def toTuple(l: List[Element]) = l match {
    case f::s::t::Nil => Some((f, s, t))
    case _else => None
  }
}
