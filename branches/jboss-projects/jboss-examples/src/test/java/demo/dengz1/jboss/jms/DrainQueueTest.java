package demo.dengz1.jboss.jms;

import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.jms.core.JmsTemplate;

public class DrainQueueTest extends AbstractJndiTest {
		
	/**
	 * Note that a received message object is a proxy to the real JMS message
	 * object. Compare to the queue browser, which gives you actual JMS
	 * message object.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testJMSPriority() throws Exception {
        ConnectionFactory cf = doLookup("ConnectionFactory");
        Queue qA = doLookup("queue/A");
        JmsTemplate jt = new JmsTemplate(cf); 
        jt.setReceiveTimeout(3000);
        
        int count = 0;
        Message msg = null;
        while ((msg = jt.receive(qA)) != null) {
			String text = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
			System.out.println(text);
			count ++;
        }
        
        System.out.println(count + " msgs drained.");
	}
}
