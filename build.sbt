import dep._

lazy val common_settings = Seq(
  organization := "cn.lovepocky",
  scalaVersion := "2.12.4",
  libraryDependencies ++= Seq(
    scalaTest % Test,
    dep.ammonite.`ammonite-ops`,
//    dep_airframe_log,
    dep_quicklens
  )
)

lazy val common_setting_logging = Seq(
  libraryDependencies ++= Seq(dep.logging.`scala-logging`) ++
    dep.logging.`logback`
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

//lazy val `airframe-log-migrate-slf4f` = (project in file("airframe-log-migrate-slf4j"))
//  .dependsOn(airframe)
//  .settings(
//    )

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
    excludeDependencies += ExclusionRule("com.lihaoyi"),
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

/*lazy val `json-scala-jack` = (project in file("json-scala-jack"))
  .settings(common_settings)
  .settings(
    useJCenter := true,
    libraryDependencies ++= Seq(
      scalajack,
      scalajack_mongo
    )
  )*/

lazy val `json-jsoniter` = (project in file("json-jsoniter"))
  .settings(common_settings)
  .settings(common_setting_logging)
  .settings(
    libraryDependencies ++= Seq(
      `jsoniter-scala`
    )
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
    dep.RaptureJson.settings_resolvers,
    libraryDependencies ++= Seq(
      `dep_scala-async`,
      `vertx-mongo-client-scala`,
      dep_joda_time
    ) ++ dep_airframe
      :+ dep.RaptureJson.dep_backend_vertx
  )

lazy val `vertx-graphql` = (project in file("vertx-graphql"))
  .settings(common_settings)
  .settings(
    fork in run := true,
    javaOptions ++= Seq("-Dvertx.logger-delegate-factory-class-name=io.vertx.core.logging.SLF4JLogDelegateFactory")
  )
  .settings(
    dep.RaptureJson.settings_resolvers,
    settings.add_resolver_engagingspaces,
    libraryDependencies ++= Seq(
//      `dep_vertx-mongo-client`,
    ) ++ dep_airframe
      ++ dep.RaptureJson.deps_common
      ++ dep.logging.`logback`
      :+ dep.logging.`scala-logging`
      :+ dep.RaptureJson.dep_backend_vertx
      :+ dep.RaptureJson.dep_backend_circe
      :+ `dep_scala-async`
      :+ dep_joda_time
      :+ dep.`sangria`
      :+ dep.`sangria-circe`
      :+ dep.`vertx-web-client-scala`
      :+ dep.`vertx-web-scala`
      :+ dep.`vertx-dataloader`
  )

lazy val `extensions-hamsters` = (project in file("extensions-hamsters"))
  .settings(common_settings)
  .settings(common_setting_logging)
  .settings(
    libraryDependencies ++= Seq(
      dep.hamsters
    )
  )

lazy val `akka-stream` = (project in file("akka-stream"))
  .settings(common_settings)
  .settings(fork in run := true)
  .settings(
    libraryDependencies ++= Seq()
      ++ dep.akka.`akka-stream`
  )

lazy val `flink-kafka` = (project in file("flink-kafka"))
  .settings(common_settings)
  .settings(common_setting_logging)
  .settings(
    ThisBuild / resolvers ++= Seq(
      "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
      Resolver.mavenLocal
    ),
    ThisBuild / scalaVersion := "2.11.12",
    scalaVersion := "2.11.12",
//assembly / mainClass := Some("org.example.Job"),
// make run command include the provided dependencies
    Compile / run := Defaults
      .runTask(
        Compile / fullClasspath,
        Compile / run / mainClass,
        Compile / run / runner
      )
      .evaluated,
// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
    Compile / run / fork := true,
    Global / cancelable := true
// exclude Scala library from assembly
//assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false),
  )
  .settings(
    libraryDependencies ++= Seq(
      dep.`typesafe-config`,
      dep.flink.`flink-connector-kafka`,
      dep.flink.`flink-scala`,
      dep.flink.`flink-streaming-scala`,
      dep.flink.`flink-runtime-web`
    ),
//    excludeDependencies += ExclusionRule(""),
    test / javaOptions += "-Dlogback.debug=true"
  )
  .settings(
    dep.logging.settings_exclude_log4j,
    libraryDependencies ++= dep.logging.deps_logging_slf4j_logback
      ++ dep.logging.`logback`
      :+ dep.logging.`scala-logging`
  )
