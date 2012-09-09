organization := "org.goldenport"

name := "goldenport-runner"

version := "0.1.0-SNAPSHOT"

// scalaVersion := "2.9.1"

crossScalaVersions := Seq("2.9.2", "2.9.1")

scalacOptions += "-deprecation"

scalacOptions += "-unchecked"

resolvers += "Asami Maven Repository" at "http://www.asamioffice.com/maven"

libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.4"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-core" % "0.4.1-seq"

libraryDependencies += "com.github.scala-incubator.io" %% "scala-io-file" % "0.4.1-seq"

libraryDependencies += "org.goldenport" %% "goldenport-sexpr" % "0.1.0"

libraryDependencies += "junit" % "junit" % "4.10" % "test"

mainClass := Some("org.goldenport.runner.Runner")

//
publishTo := Some(Resolver.file("asamioffice", file("target/maven-repository")))
