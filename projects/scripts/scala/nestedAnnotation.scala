// $ /apps/scala-2.8.0.Beta1-RC5/bin/scalac -d target -cp $CLASSPATH nestedAnnotation.scala

package deng.nestedAnnotation

import java.util._
import javax.ejb._
import javax.jms._

import org.slf4j._

trait OrderProcessor {
	def process(order: String) : Unit
}

@Stateless
//@org.jboss.annotation.ejb.LocalBinding(jndiBinding="custom/OrderProcessorImpl")
class OrderProcessorImpl extends OrderProcessor {
  val logger = LoggerFactory.getLogger(this.getClass());
  
  @Override
  def process(order: String) : Unit = {
    logger.info("Processing order: " + order);
  }
}

@MessageDriven(
  activationConfig = Array(
    new ActivationConfigProperty(propertyName="destinationType", propertyValue = "javax.jms.Queue"),
    new ActivationConfigProperty(propertyName="destination", propertyValue = "/queue/ExampleQueue"),
    new ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue = "Auto-acknowledge")
  ),
  messageListenerInterface = classOf[MessageListener]
)
class OrderReceiver extends MessageListener {
  val logger = LoggerFactory.getLogger(this.getClass())
  
  @EJB
  var orderProcessor: OrderProcessor = null
      
  @Override
  def onMessage(msg: Message) {
    try {
      logger.info("Received JMS class impl " + msg.getClass())
      logger.info("Received JMSMessageID " + msg.getJMSMessageID())
      logger.info("Received JMSCorrelationID " + msg.getJMSCorrelationID())
      logger.info("Received JMSTimestamp " + msg.getJMSTimestamp())
      logger.info("Received JMSReplyTo " + msg.getJMSReplyTo())
      logger.info("Received JMSType " + msg.getJMSType())
      
      val e = msg.getPropertyNames()
      while (e.hasMoreElements()) {
        val name = e.nextElement().asInstanceOf[String]
        logger.debug("Property " + name + ": " + msg.getObjectProperty(name))  
      }
      
      if (msg.isInstanceOf[TextMessage]) { 
        logger.info("Extracting Order to string")
        val order = (msg.asInstanceOf[TextMessage]).getText()
        
        logger.info("Pass to processor.")
        orderProcessor.process(order)
      } else {
        logger.error("Can't process non-text msg " + msg.getJMSMessageID())
      }
    } catch {
    	case e: Exception => logger.error("Failed to receive order.", e)
    }
  }
}
