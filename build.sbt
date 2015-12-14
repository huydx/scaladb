import sbt._
import sbt.Keys._
import JmhKeys._

name := "scaladb"

version := "1.0"

scalaVersion := "2.11.7"

val jmhVersion = "0.9.2"
version in Jmh := jmhVersion
fork in run := true
javaOptions in run +=  "-verbosegc"
javaOptions in run +=  "-XX:+PrintGCDetails"
javaOptions in run += "-Xloggc:gc.log"
javaOptions in run += "-Xmx700m"
javaOptions in run += "-Xmn700m"
javaOptions in run += "-XX:MaxPermSize=256M"
javaOptions in console +=  "-verbosegc"
javaOptions in console +=  "-XX:+PrintGCDetails"
javaOptions in console += "-verbosegc"
javaOptions in console += "-Xloggc:gc.log"
javaOptions in console += "-Xmx700m"
javaOptions in console += "-Xmn700m"
javaOptions in console += "-XX:MaxPermSize=256M"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4-SNAPSHOT",
  "sh.den" % "scala-offheap_2.11" % "0.1",
  "org.openjdk.jmh" % "jmh-core" % "0.9.2",
  "com.storm-enroute" %% "scalameter" % "0.7"
)

assemblyMergeStrategy in assembly := {
  case PathList("javax", "servlet", xs @ _*)         => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".properties" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".xml" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".types" => MergeStrategy.first
  case PathList(ps @ _*) if ps.last endsWith ".class" => MergeStrategy.first
  case "application.conf"                            => MergeStrategy.concat
  case "unwanted.txt"                                => MergeStrategy.discard
  case x =>
    val oldStrategy = (assemblyMergeStrategy in assembly).value
    oldStrategy(x)
}

addCompilerPlugin("org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full)
