package com.transaction.api.apitest

import akka.http.scaladsl.testkit.ScalatestRouteTest
import com.transaction.api.common.utils.ActorEssentials
import org.scalatest.{Matchers, WordSpec}
import com.transaction.api.routes.Routes

trait TransactionServiceTestSpec extends WordSpec with Matchers with ScalatestRouteTest with Routes{
  implicit val actorSystem = ActorEssentials.actorSystem
}
