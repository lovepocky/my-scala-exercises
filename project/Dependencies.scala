import sbt._

object Dependencies {
  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"

  lazy val dep_ammonite = "com.lihaoyi" % "ammonite" % "1.0.3" cross CrossVersion.full

  lazy val dep_airframe: Seq[ModuleID] = {
    val airframe_version = "0.29"
    Seq(
      "org.wvlet.airframe" %% "airframe" % airframe_version
    )
  }
}
