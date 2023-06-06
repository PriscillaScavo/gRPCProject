libraryDependencies ++= Seq(
  "com.thesamet.scalapb.zio-grpc" %% "zio-grpc-codegen"         % "0.5.1",
  "com.thesamet.scalapb"          %% "compilerplugin"           % "0.11.11",
  "com.thesamet.scalapb"          %% "scalapb-validate-codegen" % "0.3.4",
)
addSbtPlugin("com.thesamet" % "sbt-protoc" % "1.0.2")