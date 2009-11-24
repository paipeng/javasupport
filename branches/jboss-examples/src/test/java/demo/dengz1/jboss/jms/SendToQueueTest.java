package demo.dengz1.jboss.jms;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class SendToQueueTest extends AbstractJndiTest {
		
	@Test
	public void testJMSPriority() throws Exception {
        ConnectionFactory cf = doLookup("ConnectionFactory");
        Queue qA = doLookup("queue/A");
        JmsTemplate jt = new JmsTemplate(cf); 
        
        jt.send(qA, new MessageCreator(){
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage msg = session.createTextMessage("Just a test msg.");
				return msg;
			}        	
        });
	}
}
