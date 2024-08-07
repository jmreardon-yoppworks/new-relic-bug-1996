import _root_.io.github.nafg.mergify.dsl.*

// New Relic fails with 3.5.0 or greater
// Slick 3.4.1 or less works.
val slickVersion = "3.5.1"

libraryDependencies ++= List(
  "org.slf4j" % "slf4j-nop" % "2.0.13",
  "com.h2database" % "h2" % "2.2.224",
  "com.typesafe.slick" %% "slick-hikaricp" % slickVersion,
  "com.typesafe.slick" %% "slick" % slickVersion
)

scalaVersion := "2.13.14"
scalacOptions += "-deprecation"
run / fork := true

// Comment out the line below to run without the New Relic agent
run / javaOptions ++= Seq("-javaagent:opt/newrelic/newrelic-agent-8.13.0.jar")
