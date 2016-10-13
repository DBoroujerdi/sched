package github.dboroujerdi.sched.config

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigComponent {
  val config: Config
}

trait DefaultConfigComponent extends ConfigComponent {
  val config: Config = ConfigFactory.load()
}

