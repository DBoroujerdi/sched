package github.dboroujerdi.sched.scraping

import java.io.File

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.browser.JsoupBrowser.JsoupElement
import org.scalatest.FunSuite

class ScheduleParserSpec extends FunSuite {

  val browser: JsoupBrowser = new JsoupBrowser()

  val scheduleRow = browser.parseString(
    """<html>
      <head></head>
      <body>
      <table>
       <tr>
        <td><span id="bl_time_day_0_row_57"> <a id="score" class="Score">Live At</a> <span id="tzTime:br:1476311400:br:HH:mm_C:br:58">23:30 UK</span> </span></td>
          <td> <a href="http://sports.williamhill.com/bet/en-gb/betting/e/10010467/.html">Coritiba v Figueirense</a> </td>
          <td> <a href="http://sports.williamhill.com/bet/en-gb/betting/y/5/.html">Football</a> </td>
  </tr></table>
      </body>
      </html>""")

//  test("parse schedule row") {
//    println(ScheduleParser.parseRow(scheduleRow))
//  }

  test("test") {
    val file = new File("/Users/dboroujerdi/projects/sched/src/test/resources/sched_1.html")
    val doc = browser.parseFile(file)

    println(ScheduleParser.parseRows(doc))

    // todo: assertions
  }
}
