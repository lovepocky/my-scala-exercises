import dep._

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

lazy val `json-rapture-test` = (project in file("json-rapture-test"))
  .settings(common_settings)
  .settings(
    RaptureJson.settings_resolvers,
    resolvers += "Sonatype-lovepocky" at "http://jd.lovepocky.cn:8081/repository/lovepocky",
    libraryDependencies ++= Seq(
      `dep_scala-async`,
      RaptureJson.dep_backend_vertx
    ) ++ RaptureJson.deps_common :+ RaptureJson.dep_backend_jackson :+ RaptureJson.dep_backend_circe :+ dep_vertx_lang_scala :+ `dep_jackson-module-scala`
  )

lazy val `vertx-web-client` = (project in file("vertx-web-client"))
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      `dep_scala-async`,
      `vertx-web-client-scala`
    ) ++ dep_airframe
  )

lazy val `vertx-mongo-client` = (project in file("vertx-mongo-client"))
//  .dependsOn(`json-rapture`)
  .settings(common_settings)
  .settings(
    libraryDependencies ++= Seq(
      `dep_scala-async`,
      `vertx-mongo-client-scala`,
      dep_joda_time
    ) ++ dep_airframe
  )

lazy val `vertx-graphql` = (project in file("vertx-graphql"))
  .settings(common_settings)
  .settings(
    fork in run := true,
    javaOptions ++= Seq("-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory")
  )
  .settings(
    dep.RaptureJson.settings_resolvers,
    libraryDependencies ++= Seq(
//      `dep_vertx-mongo-client`,
    ) ++ dep_airframe
      ++ dep.RaptureJson.deps_common
      ++ dep.`logback`
      :+ dep.`scala-logging`
      :+ dep.RaptureJson.dep_backend_vertx
      :+ dep.RaptureJson.dep_backend_circe
      :+ `dep_scala-async`
      :+ dep_joda_time
      :+ dep.`sangria`
      :+ dep.`sangria-circe`
      :+ dep.`vertx-web-client-scala`
      :+ dep.`vertx-web-scala`
  )
