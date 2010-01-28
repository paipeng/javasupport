//mkcp '/apps/hornetq/lib/*' './out' './' '/apps/apache-activemq-5.3.0/lib/*'
val cf = new org.apache.activemq.ActiveMQConnectionFactory
val n = if (args.length == 0) 1 else args(0).toInt
JmsTest.testBurstMsg(n, cf)

