name := "actorpetrinet"

version := "0.1"

scalaVersion := "2.10.2"

mainClass in (Compile, run) := Some("org.haw.petrinet.st.Actorpetrinet")

libraryDependencies += "com.typesafe.akka" % "akka-actor_2.10" % "2.2.0"

libraryDependencies += "com.typesafe.akka" % "akka-remote_2.10" % "2.2.0"
