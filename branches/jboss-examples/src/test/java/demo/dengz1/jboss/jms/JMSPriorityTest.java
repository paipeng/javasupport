package demo.dengz1.jboss.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.ProducerCallback;

public class JMSPriorityTest extends AbstractJndiTest {		
	
	/** Sending JMS message the basic way. */
	//@Test
	public void testJMSPriorityWithoutSpring() throws Exception {
		Connection conn = null;
		Session sessioin = null;
		
		try {
	        ConnectionFactory cf = doLookup("ConnectionFactory");
	        Queue queue = doLookup("queue/A");
	        boolean isTx = false;
	        conn = cf.createConnection();
	        sessioin = conn.createSession(isTx, Session.AUTO_ACKNOWLEDGE);
	        
	        TextMessage msg = sessioin.createTextMessage();

			// Default JBOSS priority is 4. (usually it's 0-9)
			System.out.println("Default priority " + msg.getJMSPriority());
						
			MessageProducer producer = sessioin.createProducer(queue);
			
			// Send with different priority
			producer.send(msg, DeliveryMode.PERSISTENT, 9, 0);
			
			producer.close();
			
	        System.out.println("Sent msg to queue/A");
		} finally {
	        try { 
	        	sessioin.close(); 
	        } catch ( Exception e){ 
	        	throw new RuntimeException(e);
		    }
	        try { 
				conn.close();
	        } catch ( Exception e){ 
	        	throw new RuntimeException(e);
		    }
		}
	}
	

	/** Sending JMS message with Spring template. */
	@Test
	public void testJMSPriority() throws Exception {
        ConnectionFactory cf = doLookup("ConnectionFactory");
        Queue queue = doLookup("queue/A");
        JmsTemplate jt = new JmsTemplate(cf); 
        
        
        // WARN: You can't setup JBoss message priority by setting on the message!!!
        /*
        jt.send(queue, new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage msg = session.createTextMessage("Test msg with priority");
				
				//NOTE: this will NOT work!
				msg.setJMSPriority(99);
				
				return msg;
			}        	
        });
        */
        
        // A proper way is this
        jt.execute(queue, new ProducerCallback(){
			@Override
			public Object doInJms(Session session, MessageProducer producer)
					throws JMSException {
				TextMessage msg = session.createTextMessage("Test msg with priority");
				int timeToLive = 0; // 0 = forever.
				int priority = 9; // 0 = Lowest, 9 = Highest
				producer.send(msg, DeliveryMode.PERSISTENT, priority, timeToLive);
				
				// We are not working with returned object.
				return null;
			}
        });
        
        
        System.out.println("Sent msg to queue/A");
	}
}
