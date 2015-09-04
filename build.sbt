name := """PlayReactiveMongoPolymer"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation","-feature")

routesGenerator := InjectedRoutesGenerator

val reactiveMongoVersion = "0.11.6.play24"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion,
  "org.reactivemongo" %% "reactivemongo-extensions-json" % reactiveMongoVersion,
  "org.specs2" %% "specs2-core" % "2.4.9" % "test",
  "org.specs2" %% "specs2-junit" % "2.4.9" % "test"
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
