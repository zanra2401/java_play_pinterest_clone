name := """test"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

libraryDependencies += guice

libraryDependencies ++= Seq(
    javaJdbc,
    "com.mysql" % "mysql-connector-j" % "8.0.33"
)
