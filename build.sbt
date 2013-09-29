name := "petrinet"

version := "1.0"

scalaVersion := "2.10.2"

mainClass in (Compile, run) := Some("test.MyDataTest")

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2.0"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.2.0"
