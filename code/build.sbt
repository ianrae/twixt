name := "twixt"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.11.4"

libraryDependencies ++= Seq(
  javaCore, javaJdbc, javaEbean
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)


