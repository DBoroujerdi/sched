package github.dboroujerdi.sched.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.util.ByteString
import spray.json._
import cats.implicits._

import scala.concurrent.{ExecutionContext, Future}

trait ScheduleRoute extends ScheduleProtocol {
  self: ScheduleServiceComponent =>

  implicit val system: ActorSystem
  implicit val executionContext: ExecutionContext

  val route: HttpRequest => Future[HttpResponse] = {

    case HttpRequest(GET, Uri.Path("/"), _, _, _) =>
      scheduleService.fetch()
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
}
