
package nestedAnnotation

import java.util._

import javax.ejb._
import javax.jms._

import org.slf4j._

@MessageDriven(
  activationConfig = {
    new ActivationConfigProperty(propertyName="destinationType", propertyValue = "javax.jms.Queue"),
    new ActivationConfigProperty(propertyName="destination", propertyValue = "/queue/ExampleQueue"),
    new ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue = "Auto-acknowledge")
  }
)   
class OrderReceiver extends MessageListener {
  val logger = LoggerFactory.getLogger(this.getClass())
  
  @EJB
  var orderProcessor: OrderProcessor
      
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
        val name = e.nextElement()
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
