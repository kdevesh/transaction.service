package com.transaction.api.common.db

import akka.http.scaladsl.model.Uri
import com.datastax.driver.core._
import com.transaction.api.common.utils.Config

import scala.util.{Failure, Success, Try}

object ConnPool {
  val session: Session = {
    Try {
      cluster.connect(keyspace)
    } match {
      case Success(session: Session) => session
      case Failure(ex: Throwable) => throw ex
    }
  }
  private val defaultDBPort = 9042
  private val dbUri: Uri = Uri(Config.AppConfig.getString("transaction.service.cassandra.config.url"))
  private val dbHosts: Seq[String] = Uri.Query(dbUri.rawQueryString).getAll("host").::(dbUri.authority.host.toString())
  private val dbPort: Int = Some(dbUri.authority.port).getOrElse(defaultDBPort)
  private val keyspace: String = dbUri.path.toString.replaceFirst("/", "")
  private val (dbUser: String, dbPass: String) = Some(dbUri.authority.userinfo) match {
    case Some(userInfo: String) => userInfo.split(":") match {
      case Array(user: String, pass: String) => (user, pass)
      case Array(user: String) => (user, "")
    }
  }
  private val cluster: Cluster = {
    Cluster.builder()
      .addContactPoints(dbHosts: _*)
      .withCompression(ProtocolOptions.Compression.SNAPPY)
      .withPort(dbPort).withCredentials(dbUser, dbPass)
      .withQueryOptions(new QueryOptions().setConsistencyLevel(ConsistencyLevel.LOCAL_QUORUM))
      .build()
  }
}
