import sbt.Keys._

name := "sched"

lazy val commonSettings = Seq(
  version := "1.0",

  scalaVersion := "2.11.8",

  scalacOptions += "-language:experimental.macros",

  libraryDependencies ++= Seq(
    "net.ruippeixotog" %% "scala-scraper" % "1.1.0",
    "com.typesafe" % "config" % "1.3.1",
    "org.typelevel" %% "cats" % "0.7.2",
    "com.beachape" %% "enumeratum" % "1.4.17",
    "io.spray" %% "spray-json" % "1.3.2",
    "com.typesafe.akka" %% "akka-actor" % "2.4.12",
    "com.typesafe.akka" %% "akka-http-experimental" % "2.4.11",
    "com.typesafe.akka" %% "akka-stream" % "2.4.11",
    "org.scalatest" % "scalatest_2.11" % "3.0.0" % "test"
  )
)

lazy val root = project.in(file("."))
  .aggregate(web, core)
  .dependsOn(web % "test->test;compile->compile")
  .dependsOn(core % "test->test;compile->compile")
  .settings(commonSettings)

lazy val core = project.in(file("core"))
  .settings(commonSettings)

lazy val web = project.in(file("web"))
  .dependsOn(core % "test->test;compile->compile")
  .settings(commonSettings)