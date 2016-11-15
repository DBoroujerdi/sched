package github.dboroujerdi.sched.server

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.stream.scaladsl.Sink
import akka.util.ByteString
import github.dboroujerdi.sched.config.ConfigComponent
import github.dboroujerdi.sched.infrastructure.ActorSystemComponent
import github.dboroujerdi.sched.schedule.ScheduleFacadeComponent
import spray.json._

import cats.implicits._

import scala.concurrent.Future

trait WebServerComponent extends Protocols {
  self: ScheduleFacadeComponent with ActorSystemComponent with ConfigComponent =>

  val requestHandler: HttpRequest => Future[HttpResponse] = {

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      scheduleFacade.fetch()
        .map(_.toJson)
        .map(_.toString)
        .map(schedule => HttpEntity.Strict(ContentTypes.`application/json`, ByteString(schedule)))
        .value
        .map {
          case Some(res: ResponseEntity) => HttpResponse(OK, entity = res)
          case None => HttpResponse(InternalServerError, entity = "Internal Error")
          // todo need to somehow propagate errors through to the response
        }

    case _ =>
      Future(HttpResponse(NotFound, entity = "Unknown resource!"))
  }

  def startServer(): Unit = {
    val port = config.getInt("app.server.port")
    val host = config.getString("app.server.host")

    Http().bind(host, port).to(Sink.foreach { connection =>
      println("Accepted new connection from " + connection.remoteAddress)
      connection handleWithAsyncHandler requestHandler
    }).run()
  }
}
