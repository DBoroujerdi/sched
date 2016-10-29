name := "sched"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "com.bumnetworks" %% "numerato" % "0.0.1",
  "net.ruippeixotog" %% "scala-scraper" % "1.1.0",
  "com.typesafe" % "config" % "1.3.1",
  "org.typelevel" %% "cats" % "0.7.2",
  "com.beachape" %% "enumeratum" % "1.4.17",
  "io.spray" % "spray-json_2.11" % "1.3.2",
  //
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
)

scalacOptions += "-language:experimental.macros"
