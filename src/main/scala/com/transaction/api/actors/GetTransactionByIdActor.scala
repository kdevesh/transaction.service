package com.transaction.api.actors

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props}
import akka.http.scaladsl.model.HttpResponse
import com.transaction.api.actors.GetTransactionByIdActor.{GetTransactionInfoById, TransactionByIdFetched}
import com.transaction.api.actors.PerRequestActor.Done
import com.transaction.api.actors.dataactors.GetTransactionByIdDAActor
import com.transaction.api.actors.dataactors.GetTransactionByIdDAActor.FetchTransactionById
import org.slf4j.LoggerFactory

import scala.util.Random

object GetTransactionByIdActor {

  case class GetTransactionInfoById(tx_id: Long)

  case object TransactionByIdFetched

}

class GetTransactionByIdActor(completerFunction: HttpResponse => Unit, origin: ActorRef) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)

  override def supervisorStrategy: OneForOneStrategy = OneForOneStrategy() {
    case _: Exception => Escalate
  }

  override def receive = {
    case GetTransactionInfoById(tx_id) =>
      getTransactionByIdDAActor ! FetchTransactionById(tx_id)
    case TransactionByIdFetched =>
      origin ! Done
  }

  def getTransactionByIdDAActor = {
    context.actorOf(Props(new GetTransactionByIdDAActor(completerFunction)), s"getTransactionByIdDAActor-${Random.nextInt}")
  }

}
