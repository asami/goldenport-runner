seq(conscriptSettings :_*)

organization := "org.goldenport"

name := "goldenport-runner"

version := "1.0.0"

scalaVersion := "2.10.3"

// crossScalaVersions := Seq("2.9.1", "2.9.2")

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

scalacOptions += "-feature"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "7.0.5"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.2"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.2"

libraryDependencies += "org.goldenport" %% "goldenport-sexpr" % "1.0.4"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

mainClass := Some("org.goldenport.runner.Main")

// onejar
seq(com.github.retronym.SbtOneJar.oneJarSettings: _*)

mainClass in oneJar := Some("org.goldenport.runner.Main")

//
publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))
