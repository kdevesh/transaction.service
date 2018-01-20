package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import com.transaction.api.actors.GetTransactionByIdActor.TransactionByIdFetched
import com.transaction.api.actors.dataactors.GetTransactionByIdDAActor.FetchTransactionById
import com.transaction.api.common.utils.DataStore._
import com.transaction.api.models.Transaction
import com.transaction.api.models.TransactionJsonProtocol._
import org.slf4j.LoggerFactory
import spray.json._

object GetTransactionByIdDAActor {

  case class FetchTransactionById(tx_id: Long)

}

class GetTransactionByIdDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Inside GetTransactionByIdDAActor")

  override def receive = {
    case FetchTransactionById(tx_id) =>
      val result: Seq[Transaction] = tx_list.filter(tx => tx.transaction_id == tx_id)
      if (!result.isEmpty)
        completerFunction(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, result.head.toJson.toString), status = StatusCodes.OK))
      else
        completerFunction(HttpResponse(entity = s"No transaction found for tx_id = $tx_id", status = StatusCodes.NotFound))
      sender ! TransactionByIdFetched
  }
}
