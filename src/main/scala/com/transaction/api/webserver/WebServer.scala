package com.transaction.api.webserver

import akka.http.scaladsl.Http
import com.transaction.api.common.utils.ActorEssentials
import com.transaction.api.routes.Routes

object Webserver extends App with Routes {
  implicit val actorSystem = ActorEssentials.actorSystem
  implicit val materializer = ActorEssentials.materialzer
  implicit val executionContext = ActorEssentials.actorSystem.dispatcher
  val host = "localhost"
  val port = "8090"
  logger.info(s"Server Properties: Host - $host, Port - $port")
  val bindingFuture = Http().bindAndHandle(getRoutes, host, port.toInt)
  logger.info(s"Transaction Service online at http://$host:$port")
}
