package github.dboroujerdi.sched.infrastructure

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait ActorSystemComponent {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
}

trait DefaultActorSystemComponent extends ActorSystemComponent {
  implicit val system = ActorSystem("sched-system")
  implicit val materializer = ActorMaterializer()
}