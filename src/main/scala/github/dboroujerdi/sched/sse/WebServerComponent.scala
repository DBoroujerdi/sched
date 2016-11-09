package github.dboroujerdi.sched.sse

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.{Chunk, Chunked}
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{Sink, Source}
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent

trait WebServerComponent {
  self: PublisherComponent with ActorSystemComponent =>

  val requestHandler: HttpRequest => HttpResponse = {
    case HttpRequest(HttpMethods.GET, Uri.Path("/"), _, _, _) =>
      val source = Source.fromPublisher(publisher)
        .map(ServerSentEvent(_, "schedule"))
        .map(s => Chunk(s.toString))

      HttpResponse (
        entity = new Chunked(ContentTypes.`application/json`, source)
      )
    case _: HttpRequest => HttpResponse(404, entity = "Unknown resource!")
  }

  def startServer(): Unit = {
    Http().bind("127.0.0.1", 9000).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWithSyncHandler requestHandler
      // this is equivalent to
      // connection handleWith { Flow[HttpRequest] map requestHandler }
    }).run()
  }
}
