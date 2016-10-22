package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorModule
import github.dboroujerdi.sched.scraping.{ScraperModule, WebBrowserComponent}

object SchedApplication extends App
  with PollExecutorModule
  with ScraperModule
  with WebBrowserComponent
  with DefaultConfigComponent {

  poller.start
}
