package github.dboroujerdi.sched.app

import akka.http.scaladsl.Http
import akka.stream.scaladsl.Sink
import github.dboroujerdi.sched.api.ScheduleRoute
import github.dboroujerdi.sched.app.config.DefaultConfigComponent
import github.dboroujerdi.sched.app.infrastructure.DefaultActorSystemComponent
import github.dboroujerdi.sched.app.parse.stream.StreamingParserComponent
import github.dboroujerdi.sched.app.scraping.WebDocumentFetcherComponent
import github.dboroujerdi.sched.app.service.DefaultScheduleServiceComponent

trait Application extends DefaultActorSystemComponent
  with DefaultConfigComponent
  with WebDocumentFetcherComponent
  with StreamingParserComponent
  with ScheduleRoute
  with DefaultScheduleServiceComponent {

  def startServer(): Unit = {
    val port = config.getInt("app.server.port")
    val host = config.getString("app.server.host")

    Http().bind(host, port).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      connection handleWithAsyncHandler route
    }).run()
  }

  startServer()

  sys.addShutdownHook(system.terminate())
}


object DefaultApplication extends App with Application
