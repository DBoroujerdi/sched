package github.dboroujerdi.sched

import cats.data.OptionT

import scala.concurrent.Future

package object app {
  type FutureMaybe[T] = OptionT[Future, T]
}
