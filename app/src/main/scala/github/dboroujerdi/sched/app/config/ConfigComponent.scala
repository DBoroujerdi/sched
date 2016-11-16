package github.dboroujerdi.sched.app.config

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigComponent {
  val config: Config
}

trait DefaultConfigComponent extends ConfigComponent {
  lazy val config: Config = ConfigFactory.load()
}

