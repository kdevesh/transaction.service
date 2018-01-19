package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import com.transaction.api.actors.PutTransactionByIdActor.TransactionByIdSaved
import com.transaction.api.actors.dataactors.PersistTransactionByIdDAActor.SaveTransactionById
import com.transaction.api.common.utils.DataStore._
import com.transaction.api.models.Transaction

object PersistTransactionByIdDAActor {

  case class SaveTransactionById(tx: Transaction)

}

class PersistTransactionByIdDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  override def receive = {
    case SaveTransactionById(tx) =>
      val result = tx_list.filter(element => element.transaction_id == tx.transaction_id)
      if (result.isEmpty)
        tx_list = tx_list.+:(tx)
      else {
        tx_list = tx_list.filter(element => element.transaction_id != tx.transaction_id)
        tx_list = tx_list.+:(tx)
      }
      completerFunction(HttpResponse(entity = "{ status : ok }", status = StatusCodes.OK))
      sender ! TransactionByIdSaved
  }

}
