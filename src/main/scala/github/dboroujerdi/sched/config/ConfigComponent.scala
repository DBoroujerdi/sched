package github.dboroujerdi.sched.config

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigComponent {
  val config: Config
}

trait DefaultConfigComponent extends ConfigComponent {
  lazy val config: Config = ConfigFactory.load()
}

