package com.transaction.api.models

import spray.json.{DefaultJsonProtocol, DeserializationException, JsNull, JsNumber, JsObject, JsString, JsValue, RootJsonFormat}

case class Transaction(transaction_id: Long, amount: Double, tx_type: String, parent_id: Option[Long]) {
  require(amount > 0.0)
}

object TransactionJsonProtocol extends DefaultJsonProtocol {

  implicit object TransactionJsonFormat extends RootJsonFormat[Transaction] {
    override def write(tx: Transaction) =
      JsObject(
        "amount" -> JsNumber(tx.amount),
        "tx_type" -> JsString(tx.tx_type),
        "parent_id" -> tx.parent_id.map(JsNumber(_)).getOrElse(JsNull)
      )

    override def read(value: JsValue): Transaction = {
      value.asJsObject.getFields("amount", "type", "parent_id") match {
        case Seq(JsNumber(amount), JsString(tx_type), JsNumber(parent_id)) =>
          Transaction(0L, amount.toDouble, tx_type.toString, Option(parent_id.toLong))
        case Seq(JsNumber(amount), JsString(tx_type)) =>
          Transaction(0L, amount.toDouble, tx_type.toString, None)
        case _ => throw DeserializationException("Invalid Json Body")
      }
    }
  }

}