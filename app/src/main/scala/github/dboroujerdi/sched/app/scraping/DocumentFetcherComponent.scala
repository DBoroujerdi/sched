package github.dboroujerdi.sched.app.scraping

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse, StatusCodes}
import akka.util.ByteString
import cats.data.OptionT
import github.dboroujerdi.sched.api.model.Types.FutureMaybe
import github.dboroujerdi.sched.app.infrastructure.ActorSystemComponent
import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.model.Document

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

trait DocumentFetcherComponent {

  trait DocumentFetcher {
    def fetchDocument(location: String): FutureMaybe[Document]
  }

  val documentFetcher: DocumentFetcher
}

trait WebDocumentFetcherComponent extends DocumentFetcherComponent {
  this: ActorSystemComponent =>

  private final val http = Http(system)
  private final val browser = JsoupBrowser()

  class WebDocumentFetcher extends DocumentFetcher {
    override def fetchDocument(location: String): FutureMaybe[Document] = {
      OptionT(http.singleRequest(HttpRequest(uri = location))
        .flatMap {
          case HttpResponse(StatusCodes.OK, headers, entity, _) =>
            entity.dataBytes.runFold(ByteString(""))(_ ++ _)
          case HttpResponse(status, _, _, _) =>
            Future.failed(new RuntimeException("Unexpected response " + status))
        }
        .map { str =>
          Try(browser.parseString(str.decodeString("US-ASCII"))) match {
            case Success(doc) => Option(doc)
            case Failure(reason) =>
              reason.printStackTrace()
              None
          }
        })
    }
  }

  val documentFetcher: DocumentFetcher = new WebDocumentFetcher()
}