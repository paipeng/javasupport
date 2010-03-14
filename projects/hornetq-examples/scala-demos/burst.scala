// Eg: scala burst.scala 100 ExampleQueue "org.apache.activemq.ActiveMQConnectionFactory"

val n = if (args.length >= 1) args(0).toInt else 100
val q = if (args.length >= 2) args(1) else "ExampleQueue"
val jms = if (args.length >= 3) Jms.fromClassName(args(2)) else Jms.fromJndi()

new JmsTest(jms).testBurstMsg(q, n)
  
