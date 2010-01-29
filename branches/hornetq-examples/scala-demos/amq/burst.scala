//mkcp '/apps/hornetq/lib/*' './out' './' '/apps/apache-activemq-5.3.0/lib/*'
val n = if (args.length == 0) 100 else args(0).toInt
val name = "org.apache.activemq.ActiveMQConnectionFactory"
new JmsTest(Jms.fromClassName(name)).testBurstMsg("ExampleQueue", n)

