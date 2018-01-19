package com.transaction.api.actors.dataactors

import akka.actor.{Actor, ActorLogging}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCodes}
import com.transaction.api.actors.GetTypesByTypeActor.TypesByTypeFetched
import com.transaction.api.actors.dataactors.GetTypesByTypeDAActor.FetchTypesByType
import com.transaction.api.common.utils.DataStore.tx_list
import com.transaction.api.models.Transaction
import com.transaction.api.models.TransactionJsonProtocol._

import scala.concurrent.ExecutionContext.Implicits.global

object GetTypesByTypeDAActor {

  case class FetchTypesByType(tx_type: String)

}

class GetTypesByTypeDAActor(completerFunction: HttpResponse => Unit) extends Actor with ActorLogging {
  override def receive = {
    case FetchTypesByType(tx_type) =>
      val result: Seq[Transaction] = tx_list.filter(tx => tx.tx_type == tx_type)
      if (!result.isEmpty)
        Marshal(result).to[ResponseEntity].map(
          tx => completerFunction(HttpResponse(entity = tx, status = StatusCodes.OK))
        )
      else
        completerFunction(HttpResponse(entity = s"No transaction found for type = $tx_type", status = StatusCodes.OK))
      sender ! TypesByTypeFetched
  }
}
