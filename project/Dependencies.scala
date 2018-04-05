import sbt._
import sbt.Keys._

object settings {
  val add_resolver_engagingspaces = resolvers += "engagingspaces" at "https://dl.bintray.com/engagingspaces/maven"

  val jsoniter_print_codecs = scalacOptions += "-Xmacro-settings:print-codecs"
}

object Versions {
  val airframe           = "0.41"
  val coroutine          = "0.6"
  val coroutine_snapshot = "0.8-SNAPSHOT"
  val rapture_json       = "2.0.0-M9"
  val vertx              = "3.5.1"
  val scalajack          = "5.0.8"
}

object dep {

  /**
    * log
    */
  lazy val `scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

  lazy val `logback` = Seq(
    "ch.qos.logback" % "logback-core"    % "1.2.3",
    "ch.qos.logback" % "logback-classic" % "1.2.3"
  )

  lazy val dep_joda_time = "joda-time" % "joda-time" % "2.9.3"

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

  object RaptureJson {
    val settings_resolvers = resolvers ++= Seq(
      "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
      "Sonatype-lovepocky" at "http://jd.lovepocky.cn:8081/repository/lovepocky/"
    )

    val deps_common = Seq(
      "com.propensive." %% "rapture-json" % Versions.rapture_json
    )

    val dep_backend_circe   = "com.propensive" %% "rapture-json-circe"   % Versions.rapture_json
    val dep_backend_jackson = "com.propensive" %% "rapture-json-jackson" % Versions.rapture_json
    val dep_backend_vertx   = "cn.lovepocky"   %% "rapture-json-vertx"   % Versions.rapture_json
  }

  val `scalajack`        = "co.blocke" %% "scalajack"        % Versions.scalajack
  val `scalajack_mongo`  = "co.blocke" %% "scalajack_mongo"  % Versions.scalajack
  val `scalajack_dynamo` = "co.blocke" %% "scalajack_dynamo" % Versions.scalajack

  val `jsoniter-scala` = "com.github.plokhotnyuk.jsoniter-scala" %% "macros" % "0.22.2"

  val dep_vertx_lang_scala       = "io.vertx"          %% "vertx-lang-scala"         % Versions.vertx
  val `vertx-web-client-scala`   = "io.vertx"          %% "vertx-web-client-scala"   % Versions.vertx
  val `vertx-mongo-client-scala` = "io.vertx"          %% "vertx-mongo-client-scala" % Versions.vertx
  val `vertx-web-scala`          = "io.vertx"          %% "vertx-web-scala"          % Versions.vertx
  val `vertx-dataloader`         = "io.engagingspaces" % "vertx-dataloader"          % "1.0.0"

  val `dep_jackson-module-scala` = "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.9.4"

  val `sangria`       = "org.sangria-graphql" %% "sangria"       % "1.4.0"
  val `sangria-circe` = "org.sangria-graphql" %% "sangria-circe" % "1.2.1"

  val hamsters = "io.github.scala-hamsters" %% "hamsters" % "2.5.0"
}
