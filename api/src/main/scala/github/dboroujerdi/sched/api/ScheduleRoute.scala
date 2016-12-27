package github.dboroujerdi.sched.api

import akka.actor.ActorSystem
import akka.http.scaladsl.model.HttpMethods._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.util.ByteString
import spray.json._
import cats.implicits._
import github.dboroujerdi.sched.api.ScheduleService.FutureMaybe
import github.dboroujerdi.sched.api.model.Types.Schedule

import scala.concurrent.{ExecutionContext, Future}

trait ScheduleRoute extends ScheduleProtocol {
  self: ScheduleServiceComponent =>

  implicit val system: ActorSystem
  implicit val executionContext: ExecutionContext

  private def toResponse(schedule: FutureMaybe[Schedule]): Future[HttpResponse] = {
    schedule.map(_.toJson)
      .map(_.toString)
      .map(schedule => HttpEntity.Strict(ContentTypes.`application/json`, ByteString(schedule)))
      .value
      .map {
        case Some(res: ResponseEntity) => HttpResponse(OK, entity = res)
        case None => HttpResponse(InternalServerError, entity = "Internal Error")
        // todo need to somehow propagate errors through to the response
      }
  }

  val route: HttpRequest => Future[HttpResponse] = {

    case HttpRequest(GET, Uri.Path("/live"), _, _, _) =>
      toResponse(scheduleService.inPlay())

    case HttpRequest(GET, Uri.Path("/schedule"), _, _, _) =>
      toResponse(scheduleService.preMatch())

    case _ =>
      Future(HttpResponse(NotFound, entity = "Unknown resource!"))
  }
}
