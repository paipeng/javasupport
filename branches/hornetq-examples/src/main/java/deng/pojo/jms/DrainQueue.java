package deng.pojo.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class DrainQueue {
	
	public static void main(String[] args) throws Exception {
		DrainQueue main = new DrainQueue();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.timeout = Long.parseLong(System.getProperty("timeout", "5000"));
		main.run();
	}

	private String queueName;
	private long timeout = 5000; // 5 seconds.
	
	public void run() {
		try {
			drain();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void drain() throws JMSException, NamingException {
		Connection connection = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(queueName);
			System.out.println("Start to drain queue: " + queueName);
			int count = 0;
			MessageConsumer messageConsumer = session.createConsumer(queue);
			connection.start();
			
			Message msg = null;
			while ((msg = messageConsumer.receive(timeout)) != null) {
				String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
				System.out.println("msg#" + (count + 1) + " : " + msgStr);

				count ++;
			}
			System.out.println(count + " msgs removed from queue: " + queueName);
			
			session.close();
		} finally {
			if (ctx != null) {
				try { 
					ctx.close(); 
				} catch (NamingException e) { 
					/* Just log it so next clean up can proceed.*/
					System.out.println("WARN: Can not close JNDI context. " + e.getMessage());
				}
			}
			if (connection != null) {
				connection.close();
			}
		}
	}

}
