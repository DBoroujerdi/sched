package github.dboroujerdi.sched

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollExecutorModule
import github.dboroujerdi.sched.scraping.{ParserModule, WebBrowserModule}

object SchedApplication extends App
  with PollExecutorModule
  with ParserModule
  with WebBrowserModule
  with DefaultConfigComponent {

  poller.start
}
