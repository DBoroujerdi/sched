package github.dboroujerdi.sched.scraping.util

import java.net.URL

// todo see if we can add these util functions to a URL type

object UrlParseUtils {

  private object UrlExtractor {
    def unapply(in: URL) = Some((
      in.getProtocol,
      in.getHost,
      in.getPort,
      in.getPath
      ))
  }

  def extractPath(url: URL) = url match {
    case UrlExtractor(_, _, _, path) if !path.isEmpty =>
      Some(path)
    case _ =>
      None
  }

  def splitPath(path: String): Vector[String] = {
    path.split('/').toVector.tail
  }
}
