package github.dboroujerdi.sched.app.parse

import java.net.URL

package object util {

  private object UrlExtractor {
    def unapply(in: URL) = Some((
      in.getProtocol,
      in.getHost,
      in.getPort,
      in.getPath
    ))
  }

  implicit class UrlUtils(url: URL) {
    def extractPath: Option[String] = url match {
      case UrlExtractor(_, _, _, path) if !path.isEmpty =>
        Some(path)
      case _ =>
        None
    }
  }

  implicit class StringImprovements(str: String) {
    def splitBy(char: Char): Vector[String] = str.split(char).toVector
    def clean(): String = str.replace('\u00A0',' ')
    def removeWhitespace(): String = str.trim().replaceAll("\\s+","")
  }
}
