//mkcp '/apps/hornetq/lib/*' './out' './' '/apps/apache-activemq-5.3.0/lib/*'
val cf = new org.apache.activemq.ActiveMQConnectionFactory
JmsTest.testMesssageListener(cf)

