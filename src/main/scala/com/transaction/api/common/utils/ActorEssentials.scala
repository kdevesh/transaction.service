package com.transaction.api.common.utils

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

object ActorEssentials {
  implicit val actorSystem = ActorSystem("transaction-service")
  implicit val materialzer = ActorMaterializer()
}
