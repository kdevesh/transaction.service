package com.transaction.api.servicetestdata

import akka.util.ByteString
import com.transaction.api.models.Transaction

object TransactionServiceTestData {
  val putTransactionRequest1 = ByteString(
    s"""
       |{
       |    "amount": 5000, "type":"cars"
       |}
        """.stripMargin)
  val putTransactionRequest2 = ByteString(
    s"""
       |{
       |    "amount": 10000, "type":"shopping","parent_id":10
       |}
        """.stripMargin)
  val sum1 = """{ "sum" : 15000.0 }"""
  val sum2 = """{ "sum" : 10000.0 }"""
  val transactions_list = List(Transaction(0, 5000.0, "cars", None), Transaction(0, 10000.0, "shopping", Some(10)))
  val txByType1 = """[{"transaction_id":10}]"""
  val txByType2 = """[{"transaction_id":11}]"""
  val invalidTxByType = "No transaction found for type = bikes"
  val invalidTxById = "No transaction found for tx_id = 12"
  val invalidSumById = "No sum found for the requested transaction_id = 12"
}

