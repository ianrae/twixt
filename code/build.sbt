name := "twixt"

version := "0.2.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava, PlayEbean)

scalaVersion := "2.11.6"

//  "play2-crud" %% "play2-crud" % "0.7.4-SNAPSHOT",
// "play2-crud" %% "play2-crud" % "0.7.4-SNAPSHOT" classifier "assets"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.reflections" % "reflections" % "0.9.9"
)

//resolvers += "release repository" at  "http://hakandilek.github.com/maven-repo/releases/"
//
//resolvers += "snapshot repository" at "http://hakandilek.github.com/maven-repo/snapshots/"

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator


fork in run := true
