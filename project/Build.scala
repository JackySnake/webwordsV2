import com.typesafe.sbt.SbtNativePackager.packageArchetype
import sbt._
import Keys._
import com.typesafe.sbt.SbtStartScript
import com.heroku.sbt.HerokuPlugin.autoImport._

object BuildSettings {
    import Dependencies._
    import Resolvers._

    val buildOrganization = "com.typesafe"
    val buildVersion = "1.0"
    val buildScalaVersion = "2.10.4"

    packageArchetype.java_application

    val globalSettings = Seq(
        herokuJdkVersion in Compile := "1.7",
        herokuAppName in Compile := "ssr-api",
        organization := buildOrganization,
        version := buildVersion,
        scalaVersion := buildScalaVersion,
        crossScalaVersions := Seq("2.10.2", "2.10.3", "2.10.4", "2.10.5", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7"),
        scalacOptions += "-deprecation",
        fork in test := true,
        libraryDependencies ++= Seq(slf4jSimpleTest, scalatest, jettyServerTest),
//      ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
        resolvers := Seq(jbossRepo
          , akkaRepo
          , sonatypeRepo
          , snapshots
          , releases
          , typesafe
          , hseeberger
          , thenewmotion
//          , ossrh
//          , apache
          , twitter
          , finatraRepo
        ))

//  resolvers += Resolver.sonatypeRepo("snapshots"),
//  resolvers += Resolver.sonatypeRepo("releases")
//
//        resolvers += Classpaths.typesafeResolver

    val projectSettings = Defaults.defaultSettings ++ globalSettings
}

object Resolvers {
    val sonatypeRepo  = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"
    val snapshots     = "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots"
    val jbossRepo     = "JBoss" at "http://repository.jboss.org/nexus/content/groups/public/"

    val akkaRepo      = "Akka" at "http://repo.akka.io/repository/"
    val releases      = "releases"            at "http://oss.sonatype.org/content/repositories/releases"
    val typesafe      = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
    val hseeberger    = "hseeberger at bintray" at "http://dl.bintray.com/hseeberger/maven"

    val thenewmotion  = "The New Motion Public Repo" at "http://nexus.thenewmotion.com/content/groups/public/"

//  val ossrh = "NLP" at "https://oss.sonatype.org/service/local/staging/deploy/maven2/"

//  val apache = "apache opennlp releases" at "https://repository.apache.org/content/repositories/releases/"

    val twitter       = "Twitter maven" at "http://maven.twttr.com"

    val finatraRepo = "Finatra Repo" at "http://twitter.github.com/finatra"
}

object Dependencies {
    val lib             = "org.scala-lang" % "scala-library" % "2.10.4"

//    val scalatest = "org.scalatest" % "scalatest_2.10" % "2.2.5"

    val slf4jSimple     = "org.slf4j" % "slf4j-simple" % "1.6.2"
    val slf4jSimpleTest = slf4jSimple % "test"

    val jettyVersion    = "7.4.0.v20110414"
    val jettyServer     = "org.eclipse.jetty" % "jetty-server" % jettyVersion
    val jettyServlet    = "org.eclipse.jetty" % "jetty-servlet" % jettyVersion
    val jettyServerTest = jettyServer % "test"

    val asyncHttp       = "com.ning" % "async-http-client" % "1.6.5"
    val jsoup           = "org.jsoup" % "jsoup" % "1.6.1"
    val casbahCore      = "org.mongodb" % "casbah-core_2.10" % "2.8.2"
    val actor           = "com.typesafe.akka" %% "akka-actor" % "2.3.7"
    val rabbitmq        = "com.thenewmotion.akka" %% "akka-rabbitmq" % "1.2.4"
//    val stream    = "com.typesafe.akka" %%  "akka-stream-experimental" % "1.0"
//      val httpCore  = "com.typesafe.akka" % "akka-http-core-experimental_2.10" % "1.0"
//    val http      = "com.typesafe.akka" %% "akka-http-experimental" % "1.0"

    val sse       = "de.heikoseeberger" %% "akka-sse" % "1.0.0"
    val logging   = "com.typesafe.scala-logging" %%  "scala-logging-slf4j"      % "2.1.2"
    val config    = "com.typesafe" % "config" % "1.2.1"
    val rabbit    = "io.scalac" %% "reactive-rabbit" % "1.0.1"
    val logcore    = "ch.qos.logback"  %   "logback-core"             % "1.1.3"
    val logback   = "ch.qos.logback" %   "logback-classic"          % "1.1.3"
    val scalatest = "org.scalatest"              %%  "scalatest"                % "2.2.1" % "test"
    val gson      = "com.google.code.gson" % "gson" % "2.3.1"
    val commonsio = "commons-io" % "commons-io" % "2.4"
    val opennlp   = "org.apache.opennlp" % "opennlp-tools" % "1.5.3"
    val jwnl      = "net.sf.jwordnet" % "jwnl" % "1.3.3"
    val xerces    = "xerces" % "xercesImpl" % "2.9.1"
    val nekohtml  = "net.sourceforge.nekohtml" % "nekohtml" % "1.9.13"
    val fihttp    = "com.twitter" %% "finagle-http" % "6.28.0"
    val fihttpx   = "com.twitter" %% "finagle-httpx" % "6.28.0"
    val mapper    = "org.codehaus.jackson" % "jackson-mapper-asl" % "1.9.13"
    val finagle   = "com.twitter" %% "finagle-core" % "6.28.0"
    val finchCore = "com.github.finagle" %% "finch-core" % "0.8.0"
    val argonaut  = "com.github.finagle" %% "finch-argonaut" % "0.8.0"
    val jackson   = "com.github.finagle" %% "finch-jackson" % "0.8.0"
    val json4s    = "com.github.finagle" %% "finch-json4s" % "0.8.0"
    val circe     = "com.github.finagle" %% "finch-circe" % "0.8.0"

    val finatraHttp   = "com.twitter.finatra" %% "finatra-http" % "2.0.0.M2"
    val flogback      = "com.twitter.finatra" % "finatra-logback_2.10" % "2.0.0.M2"

    val json4sNative = "org.json4s" %% "json4s-native" % "3.2.10"
    val json4sJackson = "org.json4s" %% "json4s-jackson" % "3.2.10"
}

object WebWordsBuild extends Build {
    import BuildSettings._
    import Dependencies._
    import Resolvers._


//    herokuAppName in Compile := "your-heroku-app-name"

    override lazy val settings = super.settings ++ globalSettings

    lazy val root = Project("webwordsV2",
                            file("."),
                            settings = projectSettings ++
                            Seq(
                              SbtStartScript.stage in Compile := Unit
                            )) aggregate(common, web, indexer, message, mining, boilerpipe
      , demo
      , restful
      )

    lazy val web = Project("webwords-web",
                           file("web"),
                           settings = projectSettings ++
//                           SbtStartScript.startScriptForClassesSettings ++
                           Seq(libraryDependencies ++= Seq(jettyServer, jettyServlet, slf4jSimple))) dependsOn(common % "compile->compile;test->test")

    lazy val indexer = Project("webwords-indexer",
                              file("indexer"),
                              settings = projectSettings ++
//                              SbtStartScript.startScriptForClassesSettings ++
                              Seq(libraryDependencies ++= Seq(jsoup))) dependsOn(common % "compile->compile;test->test")

    lazy val common = Project("webwords-common",
                           file("common"),
                           settings = projectSettings ++
                           Seq(libraryDependencies ++= Seq(asyncHttp, casbahCore)))

    lazy val message = Project("webwords-message",
      file("rabbitmq-akka-stream"),
      settings = projectSettings ++
        Seq(libraryDependencies ++= Seq(
          actor
          , rabbit
          , logging
          , logcore
          , logback
          , scalatest
          , config
          , sse
          , rabbitmq)))

    lazy val mining = Project("webwords-mining",
      file("mining"),
      settings = projectSettings ++
        Seq(libraryDependencies ++= Seq(
          actor
          , rabbit
          , logging
          , logcore
          , logback
          , scalatest
          , config
          , sse
          , rabbitmq
          , gson
          , commonsio
          , opennlp
          , jwnl)))

  lazy val boilerpipe = Project("webwords-boilerpipe-common",
    file("boilerpipe-common"),
    settings = projectSettings ++
      Seq(libraryDependencies ++= Seq(
        actor
        , rabbit
        , logging
        , logcore
        , logback
        , scalatest
        , config
        , sse
        , rabbitmq
        , gson
        , nekohtml
        , opennlp
        , xerces)))

  lazy val demo = Project("webwords-demo",
    file("demo"),
    settings = projectSettings ++
      Seq(libraryDependencies ++= Seq(
        actor
        , rabbit
        , logging
        , logcore
        , logback
        , scalatest
        , config
        , sse
        , rabbitmq
        , gson
        , nekohtml
        , opennlp
        , xerces))) dependsOn(boilerpipe % "compile->compile;test->test", mining % "compile->compile;test->test")

    lazy val restful = Project("webwords-restful",
      file("restful-service"),
      settings = projectSettings ++
        SbtStartScript.startScriptForClassesSettings ++
        Seq(libraryDependencies ++= Seq(
          actor
          , fihttp
          , fihttpx
          , mapper
          , finchCore
          , argonaut
          , jackson
          , json4s
          , circe
          , finagle
  //        , finatra
          , finatraHttp
  //        , flogback
          , json4sNative
          , json4sJackson
        ))) dependsOn(boilerpipe % "compile->compile;test->test", mining % "compile->compile;test->test")

}

