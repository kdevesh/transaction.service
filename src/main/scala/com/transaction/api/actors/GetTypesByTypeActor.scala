package com.transaction.api.actors

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props}
import akka.http.scaladsl.model.HttpResponse
import com.transaction.api.actors.GetTypesByTypeActor.{GetTypesInfoByType, TypesByTypeFetched}
import com.transaction.api.actors.PerRequestActor.Done
import com.transaction.api.actors.dataactors.GetTypesByTypeDAActor
import com.transaction.api.actors.dataactors.GetTypesByTypeDAActor.FetchTypesByType
import org.slf4j.LoggerFactory

import scala.util.Random

object GetTypesByTypeActor {

  case class GetTypesInfoByType(tx_type: String)

  case object TypesByTypeFetched

}

class GetTypesByTypeActor(completerFunction: HttpResponse => Unit, origin: ActorRef) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Inside GetTypesByTypeActor")

  override def supervisorStrategy: OneForOneStrategy = OneForOneStrategy() {
    case _: Exception => Escalate
  }

  override def receive = {
    case GetTypesInfoByType(tx_type) =>
      getTypesByTypeDAActor ! FetchTypesByType(tx_type)
    case TypesByTypeFetched =>
      origin ! Done
  }

  def getTypesByTypeDAActor = {
    logger.info("Created GetTypesByTypeDAActor")
    context.actorOf(Props(new GetTypesByTypeDAActor(completerFunction)), s"getTypesByTypeDAActor-${Random.nextInt}")
  }
}
