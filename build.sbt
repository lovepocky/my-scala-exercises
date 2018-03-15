import Dependencies._

lazy val common_settings = Seq(
  organization := "cn.lovepocky",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    scalaTest % Test,
    dep_ammonite,
//    dep_airframe_log,
    dep_quicklens
  )
)

lazy val root = (project in file(".")).settings(
  name := "my-scala-exercise",
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

lazy val `scala-scraper` = (project in file("scala-scraper"))
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      dep_scala_scraper
    )
  )

lazy val wartremover = (project in file("wartremover"))
  .settings(common_settings)
  .settings(
    wartremoverErrors ++= Warts.unsafe
  )

lazy val `scala-async` = (project in file("scala-async"))
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      `dep_scala-async`
    )
  )
lazy val `scala-coroutines` = (project in file("scala-coroutines"))
  .settings(common_settings, scalaVersion := "2.11.11")
  .settings(
    libraryDependencies ++= Seq(
//      `dep_scala-async`
//      `dep_coroutines`
    ),
    settings_coroutines
  )

lazy val `json-rapture` = (project in file("json-rapture"))
  .settings(common_settings)
  .settings(
    RaptureJson.settings_resolvers,
    libraryDependencies ++= Seq(
      `dep_scala-async`
    ) ++ RaptureJson.deps_common :+ RaptureJson.dep_backend_jackson :+ RaptureJson.dep_backend_circe :+ dep_vertx_lang_scala :+ `dep_jackson-module-scala`
  )

lazy val `vertx-web-client` = (project in file("vertx-web-client"))
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      `dep_scala-async`,
      `dep_vertx-web-client`
    ) ++ dep_airframe
  )
