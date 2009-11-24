package demo.dengz1.jboss.jms;

import java.util.Enumeration;

import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.junit.Test;
import org.springframework.jms.core.BrowserCallback;
import org.springframework.jms.core.JmsTemplate;

public class QueueBrowserTest extends AbstractJndiTest {
	
	@Test
	public void testJMSPriority() {

        ConnectionFactory cf = doLookup("ConnectionFactory");
        Queue qA = doLookup("queue/A");
        JmsTemplate jt = new JmsTemplate(cf);  
        
        //NOTE: jt.browse("queue/A" ... will not work!
        jt.browse(qA, new BrowserCallback(){

			@Override
			public Object doInJms(Session session, QueueBrowser qbrowser)
					throws JMSException {
				Enumeration<?> e = qbrowser.getEnumeration();
		        int count = 0;
				while (e.hasMoreElements()) {
					Message msg = (Message)e.nextElement();
					String text = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
					System.out.println(text);
					System.out.println("BODY: " + ((TextMessage)msg).getText());
					count++;
				}		        
		        System.out.println(count + " msgs found.");
		        
				return null;
			}
        	
        });
	}
}
