name := "play-scala-sinch-authTicket"

version := "1.0"

scalaVersion := "2.11.6"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
  "org.mindrot" % "jbcrypt" % "0.3m"
)

scalacOptions += "-feature"