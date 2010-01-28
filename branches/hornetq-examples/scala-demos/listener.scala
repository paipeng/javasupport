import javax.jms._
val cf  = Jms.lookupJndi[ConnectionFactory](Jms.DEFAULT_CONNECTION_FACTORY_NAME)
JmsTest.testMesssageListener(cf)

