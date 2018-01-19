package com.transaction.api.actors

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.http.scaladsl.model.HttpResponse
import com.transaction.api.actors.GetSumByTransactionIdActor.GetSumInfoByTransactionId
import com.transaction.api.actors.GetTransactionByIdActor.GetTransactionInfoById
import com.transaction.api.actors.GetTypesByTypeActor.GetTypesInfoByType
import com.transaction.api.actors.PerRequestActor._
import com.transaction.api.actors.PutTransactionByIdActor.PersistTransactionById
import com.transaction.api.models.Transaction
import org.slf4j.{Logger, LoggerFactory}

import scala.util.Random

object PerRequestActor {

  case class GetTransactionByTransactionId(completerFunction: HttpResponse => Unit, tx_id: Long)

  case class GetTypesByType(completerFunction: HttpResponse => Unit, tx_type: String)

  case class GetSumByTransactionId(completerFunction: HttpResponse => Unit, tx_id: Long)

  case class PutTransactionWithTransactionId(completerFunction: HttpResponse => Unit, tx: Transaction)

  case object Done

}

class PerRequestActor extends Actor with ActorLogging {
  private val logger: Logger = LoggerFactory.getLogger(this.getClass)

  override def receive = {
    case GetTransactionByTransactionId(completerFunction, tx_id) =>
      getTransactionByTransactionIdActor(completerFunction) ! GetTransactionInfoById(tx_id)
    case GetTypesByType(completerFunction, tx_type) =>
      getTypesByTypeActor(completerFunction) ! GetTypesInfoByType(tx_type)
    case GetSumByTransactionId(completerFunction, tx_id: Long) =>
      getSumByTransactionIdActor(completerFunction) ! GetSumInfoByTransactionId(tx_id)
    case PutTransactionWithTransactionId(completerFunction, tx) =>
      putTransactionWithTransactionIdActor(completerFunction) ! PersistTransactionById(tx)
    case Done =>
      logger.info(s"Stopping Actor..............${self.path.name}")
      context.stop(self)

  }

  def getTransactionByTransactionIdActor(completerFunction: HttpResponse => Unit): ActorRef = {
    context.actorOf(Props(new GetTransactionByIdActor(completerFunction, self)), s"getTransactionByTransactionIdActor-${Random.nextInt}")
  }

  def getTypesByTypeActor(completerFunction: HttpResponse => Unit): ActorRef = {
    context.actorOf(Props(new GetTypesByTypeActor(completerFunction, self)), s"getTypesByTypeActor-${Random.nextInt}")
  }

  def getSumByTransactionIdActor(completerFunction: HttpResponse => Unit): ActorRef = {
    context.actorOf(Props(new GetSumByTransactionIdActor(completerFunction, self)), s"getSumByTransactionIdActor-${Random.nextInt}")
  }

  def putTransactionWithTransactionIdActor(completerFunction: HttpResponse => Unit): ActorRef = {
    context.actorOf(Props(new PutTransactionByIdActor(completerFunction, self)), s"putTransactionWithTransactionIdActor-${Random.nextInt}")
  }

}
