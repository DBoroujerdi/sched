name := "sched"

version := "1.0"

scalaVersion := "2.11.8"


libraryDependencies += "com.bumnetworks" %% "numerato" % "0.0.1"
libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "1.1.0"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
libraryDependencies += "com.typesafe" % "config" % "1.3.1"
libraryDependencies += "org.typelevel" %% "cats" % "0.7.2"
libraryDependencies += "com.beachape" %% "enumeratum" % "1.4.17"
libraryDependencies += "io.spray" % "spray-json_2.11" % "1.3.2"


scalacOptions += "-language:experimental.macros"
