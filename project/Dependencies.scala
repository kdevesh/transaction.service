import Versions._
import sbt._

object Dependencies {

  val akka = Seq(AkkaHttp.akkaHttp, AkkaHttp.akkaHttpCors, AkkaHttp.akkaHttpSprayJson, AkkaHttp.akkaStreamContrib, AkkaHttp.akkaTest)
  val cassandra = Seq(Cassandra.cassandraDriver, Cassandra.snappyJava, Cassandra.alpakkaConnector)
  val redis = Seq(Redis.redisScala)
  val scalaCache = Seq(ScalaCache.scalaCache)
  val logging = Seq(Logging.akkaSlf4j, Logging.logbackClassic, Logging.logstashLogbackEncoder)
  val test = Seq(Test.mockito, Test.mockitoCore, Test.scalatest1, Test.scalatest2)
  val scala = Seq(Scala.scalaUri, Scala.scalaXml)
  val utils = Seq(Utils.commanLang, Utils.commoncodec)

  object AkkaHttp {
    val akkaHttp = "com.typesafe.akka" %% "akka-http" % akkaHttpV
    val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpSprayJsonV
    val akkaHttpCors = "ch.megard" %% "akka-http-cors" % akkaHttpCorsV
    val akkaStreamContrib = "com.typesafe.akka" %% "akka-stream-contrib" % akkaStreamContribV
    val akkaTest = "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpTestKitV
  }

  object Cassandra {
    val cassandraDriver = "com.datastax.cassandra" % "cassandra-driver-core" % cassandraDriverV exclude("org.xerial.snappy", "snappy-java")
    val snappyJava = "org.xerial.snappy" % "snappy-java" % snappyJavaV
    val alpakkaConnector = "com.lightbend.akka" %% "akka-stream-alpakka-cassandra" % alpakkaCassandraConnectorV
  }

  object Redis {
    val redisScala = "com.github.etaty" %% "rediscala" % redisV
  }

  object ScalaCache {
    val scalaCache = "com.github.cb372" %% "scalacache-memcached" % "0.22.0"
  }

  object Logging {
    val akkaSlf4j = "com.typesafe.akka" %% "akka-slf4j" % akkaSlf4jV
    val logbackClassic = "ch.qos.logback" % "logback-classic" % logbackV
    val logstashLogbackEncoder = "net.logstash.logback" % "logstash-logback-encoder" % logstashV
  }

  object Scala {
    val scalaUri = "io.lemonlabs" %% "scala-uri" % scalaUriV
    val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.5"
  }

  object Test {
    val mockito = "org.mockito" % "mockito-all" % "1.8.4"
    val scalatest1 = "org.scalactic" %% "scalactic" % "3.0.1"
    val scalatest2 = "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    val mockitoCore = "org.mockito" % "mockito-core" % "2.7.22" % "test"
  }

  object Utils {
    val commanLang = "org.apache.commons" % "commons-lang3" % "3.3.2"
    val commoncodec = "commons-codec" % "commons-codec" % "1.9"
  }
}
