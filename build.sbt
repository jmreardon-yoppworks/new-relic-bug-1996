libraryDependencies ++= List(
  "org.slf4j" % "slf4j-nop" % "1.7.26",
  "com.h2database" % "h2" % "1.4.200",
  "org.scalatest" %% "scalatest" % "3.2.6" % Test
)

scalacOptions += "-deprecation"
run / fork := true
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.4.0-M1"


// based on https://stackoverflow.com/a/63780833/333643
lazy val runAll = taskKey[Unit]("Run all main classes")

def runAllIn(config: Configuration) = Def.task {
    val s = streams.value
    val cp = (config / fullClasspath).value
    val r = (run / runner).value
    val classes = (config / discoveredMainClasses).value
    classes.foreach { c =>
      s.log.info(s"Running $c")
      r.run(c, cp.files, Seq(), s.log)
    }
}

runAll := {
  runAllIn(Compile).value
  runAllIn(Test).value
}

ThisBuild / githubWorkflowBuild += WorkflowStep.Sbt(List("runAll"), name = Some(s"Run all main classes"))
