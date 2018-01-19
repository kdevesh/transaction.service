package com.transaction.api.servicetestdata

import akka.util.ByteString

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
       |    "amount": 10000, "type":"cars","parent_id":10
       |}
        """.stripMargin)
}

