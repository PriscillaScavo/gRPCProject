ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "gRPCProject"
  )


javacOptions += "-Xdoclint:all"

//akkaGrpcGeneratedLanguages := Seq(AkkaGrpc.Scala)

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http-testkit" % "10.4.0",
  "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.4.0" % Test,
  "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
  "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
  "com.typesafe.akka" %% "akka-http" % "10.4.0",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.4.0",
  "com.lightbend.akka" %% "akka-stream-alpakka-sse" % "5.0.0",
  "com.typesafe.akka" %% "akka-persistence-typed" % AkkaVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.3",

  "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapbVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion,
  "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf",
  "io.grpc" % "grpc-netty" % grpcJavaVersion,
  "com.google.protobuf" % "protobuf-java" % protobufVersion % "protobuf"

) ++ scalaTestDeps

val monocleVersion = "2.1.0"
val slickVersion = "3.4.1"
val shapelessVersion = "2.3.10"
val scalazVersion = "7.3.7"
val fs2Version = "3.6.1"
val AkkaVersion = "2.7.0"
val reactiveMongo = "1.0.10"
val zioVersion = "1.0.15"

val grpcJavaVersion = scalapb.compiler.Version.grpcJavaVersion // Last value: "1.46.0"
val protobufVersion = scalapb.compiler.Version.protobufVersion // Last value: "3.17.2"
val scalapbVersion = scalapb.compiler.Version.scalapbVersion // Last value: "0.11.10"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-test" % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
)

val scalaTestDeps = Seq(
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-shouldmatchers" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-wordspec" % "3.2.15" % Test,
  "org.scalatest" %% "scalatest-flatspec" % "3.2.15" % Test
)

Compile / PB.targets := Seq(
  scalapb.gen(grpc = true)          -> (Compile / sourceManaged).value,
  scalapb.validate.gen()            -> (Compile / sourceManaged).value,
  scalapb.zio_grpc.ZioCodeGenerator -> (Compile / sourceManaged).value
)
