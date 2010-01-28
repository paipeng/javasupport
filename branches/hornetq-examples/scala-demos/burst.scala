import javax.jms._
val cf  = Jms.lookupJndi[ConnectionFactory](Jms.DEFAULT_CONNECTION_FACTORY_NAME)
val n = if (args.length == 0) 1 else args(0).toInt
JmsTest.testBurstMsg(n, cf)

