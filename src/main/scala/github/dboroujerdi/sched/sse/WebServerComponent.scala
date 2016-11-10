package github.dboroujerdi.sched.sse

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpEntity.{Chunk, Chunked}
import akka.http.scaladsl.model._
import akka.stream.scaladsl.{Sink, Source}
import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent

import spray.json._

trait WebServerComponent extends Protocols {
  self: PublisherComponent with ActorSystemComponent with ConfigComponent =>
  val requestHandler: HttpRequest => HttpResponse = {

    case HttpRequest(HttpMethods.GET, Uri.Path("/"), _, _, _) =>
      val scheduleSource = Source.fromPublisher(publisher)
        .map(_.toJson.toString)
        .map(ServerSentEvent(_, "schedule"))
        .map(sse => Chunk(sse.toString))

      HttpResponse(entity = new Chunked(ContentTypes.`application/json`, scheduleSource))

    case _: HttpRequest =>
      HttpResponse(404, entity = "Unknown resource!")
  }

  def startServer(): Unit = {
    val port = config.getInt("app.server.port")
    val host = config.getString("app.server.host")

    Http().bind(host, port).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)

      connection handleWithSyncHandler requestHandler
      // this is equivalent to
      // connection handleWith { Flow[HttpRequest] map requestHandler }
    }).run()
  }
}
