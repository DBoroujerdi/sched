package github.dboroujerdi

import cats.data.OptionT

import scala.concurrent.Future

package object sched {
  type FutureMaybe[T] = OptionT[Future, T]
}
