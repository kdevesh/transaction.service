package com.transaction.api.routes

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.marshalling.ToResponseMarshaller
import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import ch.megard.akka.http.cors.CorsDirectives.cors
import com.transaction.api.actors.PerRequestActor
import com.transaction.api.actors.PerRequestActor.{GetSumByTransactionId, GetTransactionByTransactionId, GetTypesByType, PutTransactionWithTransactionId}
import com.transaction.api.models.Transaction
import com.transaction.api.models.TransactionJsonProtocol._
import org.slf4j.LoggerFactory
import spray.json.DeserializationException
import spray.json.JsonParser.ParsingException

import scala.util.Random

trait RouteHandlers {
  implicit def validationsRejectionHandler =
    RejectionHandler.newBuilder().handle {
      case ValidationRejection(msg, _) => complete(StatusCodes.PreconditionFailed, msg)
      case RequestEntityExpectedRejection => complete(StatusCodes.BadRequest, "RequestEntityExpectedRejection Occurred")
      case MalformedRequestContentRejection(msg, _) => complete(StatusCodes.BadRequest, msg)
      case UnsupportedRequestContentTypeRejection(_) => complete(StatusCodes.BadRequest, "UnsupportedRequestContentTypeRejection Occurred")
    }.handleNotFound {
      complete(StatusCodes.NotFound, "Requested Url Not Found")
    }.result()

  implicit def exceptionHandler = ExceptionHandler {
    case ex: IllegalArgumentException => complete(StatusCodes.PreconditionFailed, ex.getMessage)
    case DeserializationException(msg, _, _) => complete(StatusCodes.BadRequest, msg)
    case parsingEx: ParsingException => complete(StatusCodes.BadRequest, parsingEx.getMessage)
    case _ => complete(StatusCodes.BadRequest, "Bad Request")
  }
}

trait Routes extends RouteHandlers {
  val actorSystem: ActorSystem
  val logger = LoggerFactory.getLogger(this.getClass)

  def getRoutes = {
    val route =
      cors() {
        logRequestResult("transaction-service") {
          pathPrefix("transactionservice") {
            pathPrefix("transaction") {
              path(Segment) { tx_id =>
                get {
                  val perReqActor = actorSystem.actorOf(Props[PerRequestActor], s"per-req-actor-${Random.nextInt}")
                  logger.info("Created actor........" + perReqActor.path.name)
                  completeWith[HttpResponse](implicitly[ToResponseMarshaller[HttpResponse]]) {
                    completerFunction => {
                      perReqActor ! GetTransactionByTransactionId(completerFunction, tx_id.toLong)
                    }
                  }
                }
              } ~ path(Segment) { tx_id => {
                put {
                  entity(as[Transaction]) { tx =>
                    val new_tx: Transaction = tx.copy(transaction_id = tx_id.toLong)  //We create a copy of the transaction with the correct transaction_id after unmarshalling
                    val perReqActor = actorSystem.actorOf(Props[PerRequestActor], s"per-req-actor-${Random.nextInt}")
                    logger.info("Created actor........" + perReqActor.path.name)
                    completeWith[HttpResponse](implicitly[ToResponseMarshaller[HttpResponse]]) {
                      completerFunction => {
                        perReqActor ! PutTransactionWithTransactionId(completerFunction, new_tx)
                      }

                    }
                  }
                }
              }

              }
            } ~ pathPrefix("types") {
              path(Segment) { tx_type =>
                get {
                  val perReqActor = actorSystem.actorOf(Props[PerRequestActor], s"per-req-actor-${Random.nextInt}")
                  logger.info("Created actor........" + perReqActor.path.name)
                  completeWith[HttpResponse](implicitly[ToResponseMarshaller[HttpResponse]]) {
                    completerFunction => {
                      perReqActor ! GetTypesByType(completerFunction, tx_type)
                    }

                  }
                }
              }
            } ~ pathPrefix("sum") {
              path(Segment) { tx_id =>
                get {
                  val perReqActor = actorSystem.actorOf(Props[PerRequestActor], s"per-req-actor-${Random.nextInt}")
                  logger.info("Created actor........" + perReqActor.path.name)
                  completeWith[HttpResponse](implicitly[ToResponseMarshaller[HttpResponse]]) {
                    completerFunction => {
                      perReqActor ! GetSumByTransactionId(completerFunction, tx_id.toLong)
                    }
                  }
                }
              }
            }
          }
        }

      }
    route
  }
}
