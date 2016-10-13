package github.dboroujerdi.sched.scraping

import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.poller.PollerModule
import github.dboroujerdi.sched.poller.TaskExecutor._
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._
import net.ruippeixotog.scalascraper.model.Element

trait ParserModule {
  this: BrowserModule with ConfigComponent with PollerModule =>

  // todo: think it would be better to pass the Document in as an argument
  def parse(): List[(String, String, String)] = {
    val doc = browser.fetchDocument(config.getString("schedule.scrape.url"))

    // todo: tidy up and handle failures with Try and Option
    (doc >> element(".tableData"))
      .map(_ >> elementList("tr"))
      .map(_.tail) // drop the table headers
      .flatMap(_ >> elementList("td"))
      .flatMap(toTuple)
      .map({
        case (time, game, sport) =>
          (time >> element("span") >> text("span"),
            game >> (attr("href")("a"), text("a")),
            sport >> text("a"))
      })
      .map({
        case (x, y, z) => (x.toString, y.toString, z.toString)
      }).toList
  }

  private def toTuple(l: List[Element]) = l match {
    case f :: s :: t :: Nil => Some((f, s, t))
    case _else => None
  }

  val task: Task = Unit => {
    println(parse())
  }
}
