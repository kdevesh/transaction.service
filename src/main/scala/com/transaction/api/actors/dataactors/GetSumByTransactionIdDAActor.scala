package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.transaction.api.actors.dataactors.GetSumByTransactionIdDAActor.FetchSumByTransactionId
import com.transaction.api.common.utils.DataStore.tx_list
import com.transaction.api.models.Transaction

object GetSumByTransactionIdDAActor {

  case class FetchSumByTransactionId(tx_id: Long)

}

class GetSumByTransactionIdDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  override def receive = {
    case FetchSumByTransactionId(tx_id) =>
      val result: Seq[Transaction] = tx_list.filter(tx => tx.transaction_id == tx_id || tx.parent_id.getOrElse(0) == tx_id)
      var sum = 0.0
      if (!result.isEmpty) {
        result.foreach(tx => sum = sum + tx.amount)
        completerFunction(HttpResponse(entity = s"{ sum : ${sum.toString} }", status = StatusCodes.OK))
      }
      else
        completerFunction(HttpResponse(entity = s"{ sum : ${sum.toString} }", status = StatusCodes.NotFound))

  }
}
