name := "sched"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "net.ruippeixotog" %% "scala-scraper" % "1.1.0",
  "com.typesafe" % "config" % "1.3.1",
  "org.typelevel" %% "cats" % "0.7.2",
  "com.beachape" %% "enumeratum" % "1.4.17",
  "io.spray" %% "spray-json" % "1.3.2",
  "de.heikoseeberger" %% "akka-sse" % "1.11.0",
  "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
  "com.typesafe.akka" %% "akka-stream" % "2.4.11",
  "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
)

scalacOptions += "-language:experimental.macros"
