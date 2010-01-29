//mkcp '/apps/hornetq/lib/*' './out' './' '/apps/apache-activemq-5.3.0/lib/*'
val name = "org.apache.activemq.ActiveMQConnectionFactory"
new JmsTest(Jms.fromClassName(name)).testMesssageListener("ExampleQueue")

