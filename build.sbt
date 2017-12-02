
import Dependencies._

lazy val common_settings = Seq(
  libraryDependencies ++= Seq(
    scalaTest % Test,
    dep_ammonite
  )
)

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "cn.lovepocky",
      scalaVersion := "2.12.4",
      version := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test
  )

lazy val airframe = (project in file("airframe"))
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(

    ) ++ dep_airframe
  )

lazy val `airframe-log-migrate-slf4f` = (project in file("airframe-log-migrate-slf4j"))
  .dependsOn(airframe)
  .settings(

  )
