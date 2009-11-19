
package deng.hornetqexamples;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.hornetq.jms.HornetQQueue;
import org.hornetq.jms.client.HornetQConnectionFactory;
import static org.hornetq.integration.transports.netty.TransportConstants.PORT_PROP_NAME;
import org.hornetq.integration.transports.netty.NettyConnectorFactory;
import org.hornetq.core.config.TransportConfiguration;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

/**
 * Client Example from HornetQ maven example.
 * 
 * @author <a href="mailto:andy.taylor@jboss.org">Andy Taylor</a>
 */
public class ClientExample
{
   public static void main(String[] args) throws Exception
   {
      Connection connection = null;
      try
      {
         // Step 1. Directly instantiate the JMS Queue object.
         Queue queue = new HornetQQueue("ExampleQueue");

         // Step 2. Instantiate the TransportConfiguration object which contains the knowledge of what transport to use,
         // The server port etc.

         Map<String, Object> connectionParams = new HashMap<String, Object>();
         connectionParams.put(PORT_PROP_NAME, 5445);

         TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(),
                                                                                    connectionParams);

         // Step 3 Directly instantiate the JMS ConnectionFactory object using that TransportConfiguration
         ConnectionFactory cf = new HornetQConnectionFactory(transportConfiguration);

         // Step 4.Create a JMS Connection
         connection = cf.createConnection();

         // Step 5. Create a JMS Session
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

         // Step 6. Create a JMS Message Producer
         MessageProducer producer = session.createProducer(queue);

         // Step 7. Create a Text Message
         TextMessage message = session.createTextMessage("This is a text message. Date: " + new Date());

         System.out.println("Sent message: " + message.getText());

         // Step 8. Send the Message
         producer.send(message);

         // Step 9. Create a JMS Message Consumer
         MessageConsumer messageConsumer = session.createConsumer(queue);

         // Step 10. Start the Connection
         connection.start();

         // Step 11. Receive the message
         TextMessage messageReceived = (TextMessage)messageConsumer.receive(5000);

         System.out.println("Received message: " + messageReceived.getText());
      }
      finally
      {
         if (connection != null)
         {
            connection.close();
         }
      }
   }
}