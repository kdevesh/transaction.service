package com.transaction.api.common.utils


import com.datastax.driver.core.{ResultSet, Statement}
import com.transaction.api.common.db.ConnPool.session

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

trait DataAccessor extends RichListenableFuture {
  def executeAsync(stmt: Statement, fun: Function1[Try[ResultSet], Unit]) = {
    session.executeAsync(stmt).asScalaFuture.onComplete(fun)
  }
}
