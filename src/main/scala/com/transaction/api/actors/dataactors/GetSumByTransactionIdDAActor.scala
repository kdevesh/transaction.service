package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import com.transaction.api.actors.GetSumByTransactionIdActor.SumByTransactionIdFetched
import com.transaction.api.actors.dataactors.GetSumByTransactionIdDAActor.FetchSumByTransactionId
import com.transaction.api.common.utils.DataStore.tx_list
import com.transaction.api.models.Transaction
import org.slf4j.LoggerFactory

object GetSumByTransactionIdDAActor {

  case class FetchSumByTransactionId(tx_id: Long)

}

class GetSumByTransactionIdDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  private val logger = LoggerFactory.getLogger(this.getClass)
  logger.info("Inside GetSumByTransactionIdDAActor")

  override def receive = {
    case FetchSumByTransactionId(tx_id) =>
      val result: Seq[Transaction] = tx_list.filter(tx => tx.transaction_id == tx_id || tx.parent_id.getOrElse(0) == tx_id)
      if (!result.isEmpty) {
        val sum = result.foldLeft(0.0)(_ + _.amount)
        completerFunction(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`, s"""{ "sum" : ${sum.toString} }"""), status = StatusCodes.OK))
      }
      else
        completerFunction(HttpResponse(entity = s"No sum found for the requested transaction_id = ${tx_id}", status = StatusCodes.NotFound))
      sender ! SumByTransactionIdFetched
  }
}
