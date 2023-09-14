inThisBuild(List(
  description := "A Spark transformer takes in PMML",
  organization := "org.pmml4s",
  organizationHomepage := Some(new URL("https://pmml4s.org")),
  homepage := Some(url("https://github.com/autodeployai/pmml4s-spark")),
  licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
  developers := List(
    Developer(
      "scorebot",
      "Score Bot",
      "scorebot@outlook.com",
      url("https://pmml4s.org")
    )
  )
))

name := "pmml4s-spark"

startYear := Some(2017)

scalacOptions := Seq("-feature", "-language:_", "-unchecked", "-deprecation", "-encoding", "utf8")

scalacOptions in(Compile, doc) := Seq("-no-link-warnings")

crossPaths := false

libraryDependencies ++= {
  Seq(
    "org.pmml4s" %% "pmml4s" % "1.0.1",
    "org.apache.spark" %% "spark-mllib" % "3.4.1" % "provided",
    "org.scalatest" %% "scalatest" % "3.2.15" % "test",
    "junit" % "junit" % "4.12" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test"
  )
}

scalaVersion := "2.13.10"

// publishing

crossScalaVersions := Seq("2.13.10", "2.13")
