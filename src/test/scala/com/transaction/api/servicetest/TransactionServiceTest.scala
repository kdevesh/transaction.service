package com.transaction.api.servicetest

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import com.transaction.api.apitest.TransactionServiceTestSpec
import com.transaction.api.servicetestdata.TransactionServiceTestData._

class TransactionServiceTest extends TransactionServiceTestSpec {
"transactionservice/transaction/id" should {
  "add new transaction" in {
  val putRequest =HttpRequest(
    HttpMethods.PUT,
    uri = "/transactionservice/transaction/10" ,
    entity = HttpEntity(MediaTypes.`application/json`,putTransactionRequest1))

    putRequest ~> Route.seal(getRoutes) ~> check {
      response.status shouldEqual StatusCodes.OK
    }

  }
}
}
