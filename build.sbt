name := """PlayReactiveMongoPolymer"""

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.6"

scalacOptions ++= Seq("-deprecation","-feature")

routesGenerator := InjectedRoutesGenerator

val reactiveMongoVersion = "0.11.6.play24"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % reactiveMongoVersion,
  "org.reactivemongo" %% "reactivemongo-extensions-json" % reactiveMongoVersion
)

lazy val root = (project in file(".")).enablePlugins(PlayScala)
