package github.dboroujerdi.sched.app.config

import com.typesafe.config.{Config, ConfigFactory}

trait ConfigComponent {
  val inPlayUrl: String
  val preMatchUrl: String
}

trait DefaultConfigComponent extends ConfigComponent {
  lazy val config: Config = ConfigFactory.load()

  lazy val preMatchUrl: String = config.getString("schedule.pre_match.url")
  lazy val inPlayUrl: String = config.getString("schedule.in_play.url")
}

