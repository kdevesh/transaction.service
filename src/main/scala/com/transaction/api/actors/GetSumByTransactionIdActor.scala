package com.transaction.api.actors

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props}
import akka.http.scaladsl.model.HttpResponse
import com.transaction.api.actors.GetSumByTransactionIdActor.{GetSumInfoByTransactionId, SumByTransactionIdFetched}
import com.transaction.api.actors.PerRequestActor.Done
import com.transaction.api.actors.dataactors.GetSumByTransactionIdDAActor
import com.transaction.api.actors.dataactors.GetSumByTransactionIdDAActor.FetchSumByTransactionId
import org.slf4j.LoggerFactory

import scala.util.Random

object GetSumByTransactionIdActor {

  case class GetSumInfoByTransactionId(tx_id: Long)

  case object SumByTransactionIdFetched

}

class GetSumByTransactionIdActor(completerFunction: HttpResponse => Unit, origin: ActorRef) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Inside GetSumByTransactionIdActor")

  override def supervisorStrategy: OneForOneStrategy = OneForOneStrategy() {
    case _: Exception => Escalate
  }

  override def receive = {
    case GetSumInfoByTransactionId(tx_id) =>
      getSumByTransactionIdDAActor ! FetchSumByTransactionId(tx_id)
    case SumByTransactionIdFetched =>
      origin ! Done
  }

  def getSumByTransactionIdDAActor = {
    logger.info("Created GetSumByTransactionIdDAActor")
    context.actorOf(Props(new GetSumByTransactionIdDAActor(completerFunction)), s"getSumByTransactionIdDAActor${Random.nextInt}")
  }
}
