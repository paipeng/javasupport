package deng.myejb;

import java.util.*;

import javax.ejb.*;
import javax.jms.*;

import org.slf4j.*;

@MessageDriven(
  activationConfig = {
    @ActivationConfigProperty(propertyName="destinationType", propertyValue = "javax.jms.Queue"),
    @ActivationConfigProperty(propertyName="destination", propertyValue = "/queue/ExampleQueue"),
    @ActivationConfigProperty(propertyName="acknowledgeMode", propertyValue = "Auto-acknowledge")
  }
)   
public class OrderReceiver implements MessageListener {
  private Logger logger = LoggerFactory.getLogger(this.getClass());
  
  @EJB
  private OrderProcessor orderProcessor;
      
  @Override
  public void onMessage(Message msg) {
    try {
      logger.info("Received JMS class impl " + msg.getClass());
      logger.info("Received JMSMessageID " + msg.getJMSMessageID());
      logger.info("Received JMSCorrelationID " + msg.getJMSCorrelationID());
      logger.info("Received JMSTimestamp " + msg.getJMSTimestamp());
      logger.info("Received JMSReplyTo " + msg.getJMSReplyTo());
      logger.info("Received JMSType " + msg.getJMSType());
      
      Enumeration<?> e = msg.getPropertyNames();
      while (e.hasMoreElements()) {
        String name = (String)e.nextElement();
        logger.debug("Property " + name + ": " + msg.getObjectProperty(name));  
      }
      
      if (msg instanceof TextMessage) { 
        logger.info("Extracting Order to string");
        String order = ((TextMessage)msg).getText();
        
        logger.info("Pass to processor.");
        orderProcessor.process(order);
      } else {
        logger.error("Can't process non-text msg " + msg.getJMSMessageID());
      }
    } catch (Exception e) {
      logger.error("Failed to receive order.", e);
    }
  }
}
