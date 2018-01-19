package com.transaction.api.common.utils

import com.google.common.util.concurrent.{FutureCallback, Futures, ListenableFuture}

import scala.concurrent.{Future, Promise}

trait RichListenableFuture {

  implicit class RichListenableFuture[T](lf: ListenableFuture[T]) {
    def asScalaFuture: Future[T] = {
      val p = Promise[T]()
      Futures.addCallback(lf, new FutureCallback[T] {
        override def onFailure(t: Throwable): Unit = p failure t

        override def onSuccess(result: T): Unit = p success result
      })
      p.future
    }
  }

}
