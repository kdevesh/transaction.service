package com.transaction.api.actors

import akka.actor.SupervisorStrategy.Escalate
import akka.actor.{Actor, ActorLogging, ActorRef, OneForOneStrategy, Props}
import akka.http.scaladsl.model.HttpResponse
import com.transaction.api.actors.PerRequestActor.Done
import com.transaction.api.actors.PutTransactionByIdActor.{PersistTransactionById, TransactionByIdSaved}
import com.transaction.api.actors.dataactors.PersistTransactionByIdDAActor
import com.transaction.api.actors.dataactors.PersistTransactionByIdDAActor.SaveTransactionById
import com.transaction.api.models.Transaction
import org.slf4j.LoggerFactory

import scala.util.Random

object PutTransactionByIdActor {

  case class PersistTransactionById(tx: Transaction)

  case object TransactionByIdSaved

}

class PutTransactionByIdActor(completerFunction: HttpResponse => Unit, origin: ActorRef) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Inside PutTransactionByIdActor")

  override def supervisorStrategy: OneForOneStrategy = OneForOneStrategy() {
    case _: Exception => Escalate
  }

  override def receive = {
    case PersistTransactionById(tx) =>
      getPersistTransactionByIdDAActor ! SaveTransactionById(tx)
    case TransactionByIdSaved =>
      origin ! Done
  }

  def getPersistTransactionByIdDAActor = {
    logger.info("Created PersistTransactionByIdDAActor")
    context.actorOf(Props(new PersistTransactionByIdDAActor(completerFunction)), s"persistTxByIdActor-${Random.nextInt}")
  }
}
