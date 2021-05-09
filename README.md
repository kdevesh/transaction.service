# My selected submission for a coding challenge (transaction.service)

## 0.1 Coding Challenge:

A RESTful web service that stores some transactions
(in memory is fine) and returns information about those transactions. The
transactions to be stored have a type and an amount. The service should support
returning all transactions of a type. Also, transactions can be linked to each
other (using a ”parent id”) and we need to know the total amount involved for
all transactions linked to a particular transaction.

## 0.2 Api Spec
#### PUT /transactionservice/transaction/$transaction_id
**Body:**
{ "amount":double,"type":string,"parent_id":long }
<br />

**• transaction id** is a long specifying a new transaction <br />
**• amount** is a double specifying the amount <br />
**• type** is a string specifying a type of the transaction. <br />
**• parent id** is an optional long that may specify the parent transaction of this transaction. <br />


#### GET /transactionservice/transaction/$transaction_id
**Returns:** { "amount":double,"type":string,"parent_id":long }
#### GET /transactionservice/types/$type
**Returns:** [long, long, ... ]
A json list of all transaction ids that share the same type $type.
#### GET /transactionservice/sum/$transaction_id
**Returns:** { "sum": double }
A sum of all transactions that are transitively linked by their parent_id to $transaction_id.

## 0.3 Examples

#### PUT /transactionservice/transaction/10 
{ "amount": 5000, "type":"cars" }
=> { "status": "ok" }
#### PUT /transactionservice/transaction/11
{ "amount": 10000, "type": "shopping", "parent_id": 10}
=> { "status": "ok" }
#### GET /transactionservice/types/cars 
=> [10]
#### GET /transactionservice/sum/10 
=> {"sum":15000}
#### GET /transactionservice/sum/11
=> {"sum":10000}
