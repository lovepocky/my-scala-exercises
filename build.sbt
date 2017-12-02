
import Dependencies._

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
  .settings(
    libraryDependencies ++= Seq(

    ) ++ dep_airframe
  )
