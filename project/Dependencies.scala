import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"

  lazy val dep_ammonite = "com.lihaoyi" % "ammonite" % "1.0.3" cross CrossVersion.full

  val airframe_version = "0.29"
  lazy val dep_airframe: Seq[ModuleID] = Seq(
    "org.wvlet.airframe" %% "airframe" % airframe_version
  )

  lazy val dep_airframe_log = "org.wvlet.airframe" %% "airframe-log" % airframe_version

  lazy val dep_scala_scraper = "net.ruippeixotog" %% "scala-scraper" % "2.0.0"

  lazy val dep_quicklens = "com.softwaremill.quicklens" %% "quicklens" % "1.4.11"
}
