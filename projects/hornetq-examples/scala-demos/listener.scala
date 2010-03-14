// Eg: scala listener.scala ExampleQueue "org.apache.activemq.ActiveMQConnectionFactory"

val q = if (args.length >=1) args(0) else "ExampleQueue"
val jms = if (args.length >= 2) Jms.fromClassName(args(1)) else Jms.fromJndi()

new JmsTest(jms).testMesssageListener(q)

