name := "transaction.service"

version := "0.1"

scalaVersion := Versions.scalaV

libraryDependencies ++= Dependencies.akka ++ Dependencies.cassandra ++ Dependencies.redis ++ Dependencies.scalaCache ++ Dependencies.logging ++ Dependencies.scala ++ Dependencies.test ++ Dependencies.utils
        