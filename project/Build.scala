import sbt._
import Keys._
import com.typesafe.sbt.SbtStartScript

object BuildSettings {
    import Dependencies._
    import Resolvers._

    val buildOrganization = "com.typesafe"
    val buildVersion = "1.0"
    val buildScalaVersion = "2.10.4"

    val globalSettings = Seq(
        organization := buildOrganization,
        version := buildVersion,
        scalaVersion := buildScalaVersion,
        crossScalaVersions := Seq("2.9.0.1", "2.10.2", "2.10.3", "2.10.4", "2.10.5", "2.11.0", "2.11.1", "2.11.2", "2.11.3", "2.11.4", "2.11.5", "2.11.6", "2.11.7"),
        scalacOptions += "-deprecation",
        fork in test := true,
        libraryDependencies ++= Seq(slf4jSimpleTest, scalatest, jettyServerTest),
//      ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) },
        resolvers := Seq(jbossRepo, akkaRepo, sonatypeRepo, snapshots, releases, typesafe))

//  resolvers += Resolver.sonatypeRepo("snapshots"),
//  resolvers += Resolver.sonatypeRepo("releases")
//
//        resolvers += Classpaths.typesafeResolver

    val projectSettings = Defaults.defaultSettings ++ globalSettings
}

object Resolvers {
    val sonatypeRepo = "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases"
    val jbossRepo = "JBoss" at "http://repository.jboss.org/nexus/content/groups/public/"
    val akkaRepo = "Akka" at "http://repo.akka.io/repository/"

  val snapshots = "snapshots"           at "http://oss.sonatype.org/content/repositories/snapshots"
  val releases = "releases"            at "http://oss.sonatype.org/content/repositories/releases"
  val typesafe = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
}

object Dependencies {
//    val scalatest = "org.scalatest" % "scalatest_2.10" % "2.2.5"
    val slf4jSimple = "org.slf4j" % "slf4j-simple" % "1.6.2"
    val slf4jSimpleTest = slf4jSimple % "test"

    val jettyVersion = "7.4.0.v20110414"
    val jettyServer = "org.eclipse.jetty" % "jetty-server" % jettyVersion
    val jettyServlet = "org.eclipse.jetty" % "jetty-servlet" % jettyVersion
    val jettyServerTest = jettyServer % "test"

    val asyncHttp = "com.ning" % "async-http-client" % "1.6.5"

    val jsoup = "org.jsoup" % "jsoup" % "1.6.1"

    val casbahCore = "org.mongodb" % "casbah-core_2.10" % "2.8.2"

  val actor = "com.typesafe.akka" %% "akka-actor" % "2.3.7"
  val stream    = "com.typesafe.akka" %%  "akka-stream-experimental" % "0.10"
  val rabbit    = "io.scalac" %%  "reactive-rabbit"          % "0.2.1"
  val logging   = "com.typesafe.scala-logging" %%  "scala-logging-slf4j"      % "2.1.2"
  val logbak    = "ch.qos.logback"  %   "logback-core"             % "1.1.2"
  val logcore   = "ch.qos.logback" %   "logback-classic"          % "1.1.2"
  val scalatest = "org.scalatest"              %%  "scalatest"                % "2.2.1" % "test"

}

object WebWordsBuild extends Build {
    import BuildSettings._
    import Dependencies._
    import Resolvers._

    override lazy val settings = super.settings ++ globalSettings

    lazy val root = Project("webwords",
                            file("."),
                            settings = projectSettings ++
                            Seq(
                              SbtStartScript.stage in Compile := Unit
                            )) aggregate(common, web, indexer, message)

    lazy val web = Project("webwords-web",
                           file("web"),
                           settings = projectSettings ++
                           SbtStartScript.startScriptForClassesSettings ++
                           Seq(libraryDependencies ++= Seq(jettyServer, jettyServlet, slf4jSimple))) dependsOn(common % "compile->compile;test->test")

    lazy val indexer = Project("webwords-indexer",
                              file("indexer"),
                              settings = projectSettings ++
                              SbtStartScript.startScriptForClassesSettings ++
                              Seq(libraryDependencies ++= Seq(jsoup))) dependsOn(common % "compile->compile;test->test")

    lazy val common = Project("webwords-common",
                           file("common"),
                           settings = projectSettings ++
                           Seq(libraryDependencies ++= Seq(asyncHttp, casbahCore)))

    lazy val message = Project("webwords-message",
      file("rabbitmq-akka-stream"),
      settings = projectSettings ++
        Seq(libraryDependencies ++= Seq(actor, stream, rabbit, logging, logbak, logcore, scalatest)))
}

