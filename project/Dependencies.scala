import sbt._
import sbt.Keys._

object settings {
  val add_resolver_engagingspaces = resolvers += "engagingspaces" at "https://dl.bintray.com/engagingspaces/maven"

  val jsoniter_print_codecs = scalacOptions += "-Xmacro-settings:print-codecs"
}

object Versions {
  val ammonite           = "1.1.2"
  val airframe           = "0.41"
  val coroutine          = "0.6"
  val coroutine_snapshot = "0.8-SNAPSHOT"
  val rapture_json       = "2.0.0-M9"
  val vertx              = "3.5.1"
  val scalajack          = "5.0.8"
  val akka               = "2.5.12"
  val `akka-http`        = "10.1.1"
  val flink              = "1.5.0"

}

object dep {

  /**
    * log
    */
  object logging {

    lazy val settings_exclude_log4j = Seq(
      libraryDependencies := libraryDependencies.value
        .map(
          _ exclude ("org.apache.logging.log4j", "log4j-slf4j-impl") //slf4j -> log4j2(impl)
            exclude ("org.apache.logging.log4j", "log4j")            //log4j2(impl)
            exclude ("org.apache.logging.log4j", "log4j-core")       //log4j2(impl)
            exclude ("org.apache.logging.log4j", "log4j-jcl")        //jcl -> log4j2(impl)
            exclude ("org.apache.logging.log4j", "log4j-jul")        //jul -> log4j2(impl)
            exclude ("org.apache.logging.log4j", "log4j-1.2-api")    //log4j1.2 -> log4j2(impl)
          //# log4j12
            exclude ("org.slf4j", "slf4j-log4j12") //slf4j -> log4j12(impl)
            exclude ("log4j", "log4j")             //log4j1.2(impl)
          //# jcl
            exclude ("org.slf4j", "slf4j-jcl") //slf4j -> jcl
          //# jul
            exclude ("org.slf4j", "slf4j-jdk14") // slf4j -> jul
        ),
      excludeDependencies += "org.slf4j"       %% "slf4j-log4j12",
      excludeDependencies += "net.jpountz.lz4" % "lz4"
    )

    lazy val deps_logging_slf4j_logback = Seq(
      //slf4j(interface)
      "org.slf4j" % "slf4j-api" % "1.7.25",
      //log4j12 -> slf4j
      "org.slf4j" % "log4j-over-slf4j" % "1.7.25",
      //log4j2 -> slf4j
      "org.apache.logging.log4j" % "log4j-to-slf4j" % "2.9.1",
      //jul -> slf4j
      "org.slf4j" % "jul-to-slf4j" % "1.7.25",
      //jcl -> slf4j
      "org.slf4j" % "jcl-over-slf4j" % "1.7.25"

      //slf4j impl: logback
      //    "ch.qos.logback" % "logback-core"    % "1.2.3",
      //    "ch.qos.logback" % "logback-classic" % "1.2.3"
    )

    lazy val `scala-logging` = "com.typesafe.scala-logging" %% "scala-logging" % "3.7.2"

    lazy val `logback` = Seq(
      "ch.qos.logback" % "logback-core"    % "1.2.3",
      "ch.qos.logback" % "logback-classic" % "1.2.3"
    )
  }

  lazy val `typesafe-config` = "com.typesafe" % "config" % "1.3.2"

  lazy val dep_joda_time = "joda-time" % "joda-time" % "2.9.3"

  lazy val scalaTest = "org.scalatest" %% "scalatest" % "3.0.3"

  object ammonite {
    lazy val ammonite = "com.lihaoyi" %% "ammonite" % Versions.ammonite cross CrossVersion.binary

    lazy val `ammonite-ops` = "com.lihaoyi" %% "ammonite-ops" % Versions.ammonite cross CrossVersion.binary
  }

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

  object akka {

    /**
      * https://akka.io/docs/
      */
    val `akka-actor` = Seq(
      "com.typesafe.akka" %% "akka-actor"   % Versions.akka,
      "com.typesafe.akka" %% "akka-testkit" % Versions.akka % Test
    )
    val `akka-stream` = Seq(
      "com.typesafe.akka" %% "akka-stream"         % Versions.akka,
      "com.typesafe.akka" %% "akka-stream-testkit" % Versions.akka % Test
    )
    val `akka-http` = Seq(
      "com.typesafe.akka" %% "akka-http"         % Versions.`akka-http`,
      "com.typesafe.akka" %% "akka-http-testkit" % Versions.`akka-http` % Test
    )

    val `akka-cluster`          = "com.typesafe.akka" %% "akka-cluster"          % Versions.akka
    val `akka-cluster-sharding` = "com.typesafe.akka" %% "akka-cluster-sharding" % Versions.akka
    val `akka-distributed-data` = "com.typesafe.akka" %% "akka-distributed-data" % Versions.akka
    val `akka-persistence`      = "com.typesafe.akka" %% "akka-persistence"      % Versions.akka
  }

  object flink {
    val `flink-connector-kafka` = "org.apache.flink" %% "flink-connector-kafka-0.11" % Versions.flink
    val `flink-scala`           = "org.apache.flink" %% "flink-scala"                % Versions.flink % "provided"
    val `flink-streaming-scala` = "org.apache.flink" %% "flink-streaming-scala"      % Versions.flink % "provided"
    val `flink-runtime-web`     = "org.apache.flink" %% "flink-runtime-web"          % Versions.flink % "provided"
  }

}
