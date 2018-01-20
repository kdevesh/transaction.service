package com.transaction.api.servicetestcases

import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Route
import com.transaction.api.apitest.TransactionServiceTestSpec
import com.transaction.api.models.Transaction
import com.transaction.api.servicetestdata.TransactionServiceTestData._

class TransactionServiceTest extends TransactionServiceTestSpec {
  "PUT transactionservice/transaction/10" should {
    "add new transaction" in {
      val putRequest = HttpRequest(
        HttpMethods.PUT,
        uri = "/transactionservice/transaction/10",
        entity = HttpEntity(MediaTypes.`application/json`, putTransactionRequest1))

      putRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
      }
    }
  }
  "PUT transactionservice/transaction/11" should {
    "add new transaction" in {
      val putRequest = HttpRequest(
        HttpMethods.PUT,
        uri = "/transactionservice/transaction/11",
        entity = HttpEntity(MediaTypes.`application/json`, putTransactionRequest2))

      putRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
      }
    }
  }
  "Get transactionservice/transaction/10" should {
    "get the details of the requested transaction_id" in {
      import com.transaction.api.models.TransactionJsonProtocol._
      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/transaction/10",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[Transaction] shouldEqual transactions_list(0)
      }
    }
  }
  "Get transactionservice/transaction/11" should {
    "get the details of the requested transaction_id" in {
      import com.transaction.api.models.TransactionJsonProtocol._
      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/transaction/11",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[Transaction] shouldEqual transactions_list(1)
      }
    }
  }
  "Get transactionservice/sum/10" should {
    "get the sum of all the transactions with parent_id=10 && transaction_id = 10" in {
      import com.transaction.api.models.TransactionJsonProtocol._
      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/sum/10",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual sum1
      }
    }
  }
  "Get transactionservice/sum/11" should {
    "get the sum of all the transactions with parent_id=11 && transaction_id = 11" in {
      import com.transaction.api.models.TransactionJsonProtocol._
      import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/sum/11",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual sum2
      }
    }
  }
  "Get transactionservice/types/cars" should {
    "get all the transactions of type cars" in {
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/types/cars",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual txByType1
      }
    }
  }
  "Get transactionservice/types/shopping" should {
    "get all the transactions of type shopping" in {
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/types/shopping",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual txByType2
      }
    }
  }
  "Get transactionservice/types/bikes" should {
    "get all the transactions of type bikes" in {
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/types/bikes",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual invalidTxByType
      }
    }
  }
  "Get transactionservice/transaction/12" should {
    "get the transaction of id 12" in {
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/transaction/12",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual invalidTxById
      }
    }
  }
  "Get transactionservice/sum/12" should {
    "get the sum of all the transactions with parent_id=12 && transaction_id = 12" in {
      val getRequest = HttpRequest(
        HttpMethods.GET,
        uri = "/transactionservice/sum/12",
      )
      getRequest ~> Route.seal(getRoutes) ~> check {
        response.status shouldEqual StatusCodes.NotFound
        responseAs[String] shouldEqual invalidSumById
      }
    }
  }
}
