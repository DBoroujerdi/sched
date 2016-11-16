package github.dboroujerdi.sched.app.infrastructure

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

import scala.concurrent.ExecutionContext

trait ActorSystemComponent {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
  implicit val executionContext: ExecutionContext
}

trait DefaultActorSystemComponent extends ActorSystemComponent {
  implicit val system: ActorSystem = ActorSystem("sched-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val executionContext: ExecutionContext = system.dispatcher
}