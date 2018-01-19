package com.transaction.api.common.cache

import com.transaction.api.models.Transaction

import scalacache._
import scalacache.memcached._
import scalacache.serialization.binary._

object ScalaCache {
  implicit val txCache: Cache[Transaction] = MemcachedCache("localhost:11211")
}
