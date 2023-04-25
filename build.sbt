ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "gRPCProject"
  )

enablePlugins(AkkaGrpcPlugin)

javacOptions += "-Xdoclint:all"

akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala)

libraryDependencies ++= Seq(
  "com.google.protobuf" % "protobuf-java" % akka.grpc.gen.BuildInfo.googleProtobufVersion % "protobuf",
  "com.typesafe.akka" %% "akka-http-testkit" % "10.4.0",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.4.0" % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.4.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.4.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-sse" % "5.0.0",
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3"
) ++ scalaTestDeps

val monocleVersion = "2.1.0"
val slickVersion = "3.4.1"
val shapelessVersion = "2.3.10"
val scalazVersion = "7.3.7"
val fs2Version = "3.6.1"
val AkkaVersion = "2.7.0"
val reactiveMongo = "1.0.10"

val scalaTestDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-wordspec" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-flatspec" % "3.2.15" % Test
)