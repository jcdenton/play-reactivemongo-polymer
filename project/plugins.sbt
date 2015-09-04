resolvers += "Typesafe repository" at "https://repo.typesafe.com/typesafe/releases/"

// The Play plugin
addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.4.0")

// This plugin adds commands to generate IDE project files
addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")
