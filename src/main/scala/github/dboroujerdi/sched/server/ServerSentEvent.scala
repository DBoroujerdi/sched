package github.dboroujerdi.sched.server

import java.nio.charset.StandardCharsets._

import akka.util.ByteString

final case class ServerSentEvent(data: String, event: String) {

  val heartBeat = ByteString("\n", UTF_8.name)

  def encode(): ByteString = ByteString(toString, UTF_8.name)

  override def toString: String = {
    val sb = new StringBuilder
    sb.append("event: ")
    sb.append(event)
    sb.append("\n")
    sb.append("data: ")
    sb.append(data)
    sb.append("\n\n")
    sb.toString()
  }
}
