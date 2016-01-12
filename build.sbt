name := "checkout-kata"

version := "1.0"

scalaVersion := "2.11.7"

//scalacOptions ++= Seq("-Xprint:typer")

libraryDependencies ++= Seq(
  "org.spire-math" %% "cats" % "0.4.0-SNAPSHOT",
  "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
)