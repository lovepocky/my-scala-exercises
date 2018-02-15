import sbt._
import sbt.Keys._

object Versions {
  val airframe  = "0.29"
  val coroutine = "0.6"
  val coroutine_snapshot = "0.8-SNAPSHOT"
}

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"

  lazy val dep_ammonite = "com.lihaoyi" % "ammonite" % "1.0.3" cross CrossVersion.full

  lazy val dep_airframe = Seq(
    "org.wvlet.airframe" %% "airframe" % Versions.airframe
  )

  lazy val dep_airframe_log = "org.wvlet.airframe" %% "airframe-log" % Versions.airframe

  lazy val dep_scala_scraper = "net.ruippeixotog" %% "scala-scraper" % "2.0.0"

  lazy val dep_quicklens = "com.softwaremill.quicklens" %% "quicklens" % "1.4.11"

  lazy val `dep_scala-async` = "org.scala-lang.modules" %% "scala-async" % "0.9.7"

  lazy val `dep_coroutines` = "com.storm-enroute" %% "coroutines" % Versions.coroutine

  lazy val settings_coroutines = Seq(
    resolvers ++= Seq(
      "Sonatype OSS Snapshots" at
        "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype OSS Releases" at
        "https://oss.sonatype.org/content/repositories/releases"
    ),
    libraryDependencies ++= Seq("com.storm-enroute" %% "coroutines" % Versions.coroutine_snapshot)
  )
}
