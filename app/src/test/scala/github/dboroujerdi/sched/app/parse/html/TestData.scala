package github.dboroujerdi.sched.app.parse.html

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document


object TestData {
  lazy val browser: JsoupBrowser = new JsoupBrowser()

  lazy val examplePreMatchSchedule: Document = browser.parseResource("/pre-match-schedule.html")
  lazy val exampleInPlaySchedule: Document = browser.parseResource("/in-play-football-schedule.html")
}
