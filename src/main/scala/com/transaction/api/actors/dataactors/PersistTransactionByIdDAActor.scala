package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.model.{HttpEntity, HttpResponse, MediaTypes, StatusCodes}
import com.transaction.api.actors.PutTransactionByIdActor.TransactionByIdSaved
import com.transaction.api.actors.dataactors.PersistTransactionByIdDAActor.SaveTransactionById
import com.transaction.api.common.utils.DataStore._
import com.transaction.api.models.Transaction

import scala.util.{Failure, Success, Try}

object PersistTransactionByIdDAActor {

  case class SaveTransactionById(tx: Transaction)

}

class PersistTransactionByIdDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  override def receive = {
    case SaveTransactionById(tx) =>
      Try {
        val result = tx_list.filter(element => element.transaction_id == tx.transaction_id)
        if (result.isEmpty)
          tx_list = tx_list.+:(tx)
        else {
          tx_list = tx_list.filter(element => element.transaction_id != tx.transaction_id)
          tx_list = tx_list.+:(tx)
        }
      } match {
        case Success(value) => completerFunction(HttpResponse(entity = HttpEntity(MediaTypes.`application/json`,"""{ "status" : "ok" }"""), status = StatusCodes.OK))
        case Failure(ex) => completerFunction(HttpResponse(entity = s"Some Exception occurred-${ex.getMessage}", status = StatusCodes.BadRequest))
      }
      sender ! TransactionByIdSaved
  }

}
