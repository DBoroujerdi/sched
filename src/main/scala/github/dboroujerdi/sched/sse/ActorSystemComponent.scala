package github.dboroujerdi.sched.sse

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

trait ActorSystemComponent {
  implicit val system: ActorSystem
  implicit val materializer: ActorMaterializer
}

trait DefaultActorSystemComponent extends ActorSystemComponent {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
}