package com.transaction.api.common.utils

import com.transaction.api.models.Transaction

object DataStore {
  var tx_list = List[Transaction]()  //It maintains the list of all the transactions inserted in-memory.
}
