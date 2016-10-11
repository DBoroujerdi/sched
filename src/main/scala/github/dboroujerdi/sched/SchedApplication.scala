package github.dboroujerdi.sched

object SchedApplication extends App
  with JobExecutor
  with Scraper {

  def executeJob() = {
    val url: String = "http://sports.williamhill.com/bet/en-gb/betlive/schedule"

    val res = scrape(url)
    println(res)
  }
}
