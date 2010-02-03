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

public class QueueDrainer {
	
	public static void main(String[] args) throws Exception {
		QueueDrainer main = new QueueDrainer();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.timeout = Long.parseLong(System.getProperty("timeout", "5000"));
		main.run();
	}

	private String queueName;
	private long timeout = 5000; // 5 seconds.
	
	public void run() {
		Connection connection = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Queue queue = (Queue)ctx.lookup(queueName);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			System.out.println("Start to drain queue: " + queueName);
			int count = 0;
			MessageConsumer messageConsumer = session.createConsumer(queue);
			Message msg = null;
			while ((msg = messageConsumer.receive(timeout)) != null) {
				String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
				System.out.println("=== msg#" + (count + 1) + " ===");
				System.out.println(msgStr);
				System.out.println("===============================");

				count ++;
			}
			System.out.println(count + " msgs removed from queue: " + queueName);
			
			session.close();
		} catch (JMSException e) {
			throw new RuntimeException(e);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		} finally {
			if (ctx != null) {
				try { ctx.close(); } catch (NamingException e) { throw new RuntimeException(e); }
			}
			if (connection != null) {
				try { connection.close(); } catch (JMSException e) { throw new RuntimeException(e); }
			}
		}
	}

}
