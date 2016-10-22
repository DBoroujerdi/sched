package github.dboroujerdi.sched.scraping

import github.dboroujerdi.sched.config.DefaultConfigComponent
import github.dboroujerdi.sched.poller.PollerModule
import github.dboroujerdi.sched.poller.TaskExecutor._
import org.scalatest.FunSuite

class ScraperModuleSpec extends FunSuite
  with ScraperModule
  with FileBrowserComponent
  with SinglePollModule
  with DefaultConfigComponent {


}

trait SinglePollModule extends PollerModule {
  val task: Task
}

trait FileBrowserComponent extends BrowserComponent {

  class FileBrowser extends Browser {
    override def fetchDocument(name: String) = {
      browser.parseFile(name)
    }
  }

  val browser: Browser = new FileBrowser()
}
