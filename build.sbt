ThisBuild / scalaVersion := "3.3.1"

ThisBuild / version := "0.1.0-SNAPSHOT"

lazy val scala213 = "2.13.12"
lazy val scala3 = "3.3.1"
lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := """playreal""",
    scalaVersion := scala213,
    crossScalaVersions := Seq(scala213, scala3),
    libraryDependencies ++= Seq(
      guice,
      jdbc,
      evolutions,
      "com.h2database" % "h2" % "2.2.224",
      "org.playframework.anorm" %% "anorm" % "2.7.0",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.1.0" % Test
    )
  )
