package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorComponent
import github.dboroujerdi.sched.scraping.{ScraperComponent, WebBrowserComponent}

object SchedApplication extends App
  with PollExecutorComponent
  with ScraperComponent
  with WebBrowserComponent
  with DefaultConfigComponent {

  taskExecutor.start { () =>
    println("bleep")
  }
}
