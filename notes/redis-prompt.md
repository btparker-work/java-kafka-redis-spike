in redis I need an ordered set of exception records with filtering tags that I will hold in a hash per exception.  I need to use the jedis library in java to read cache and a junit test that can populate my redis cache with test data I can query.

I have a list of exceptions where each element follows the structure below.  How do I store an exception in a ZSET named FINANCE_QUEUE?  How do I score this considering DaysInQueue, OrderPriority, and NeedByDate?

Exception
  ExceptionId: 123123123
  ItemNumber: 123123123
  DaysInQueue: .25
  OrderPriority: Low
  NeedByDate: 2025/02/20
  tags:
    epostrx
    Finance_Queue
    Default_Filter