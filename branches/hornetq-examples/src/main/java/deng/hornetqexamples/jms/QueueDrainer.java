package deng.hornetqexamples.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueueDrainer {
	
	public static void main(String[] args) throws Exception {
		QueueDrainer bean = new QueueDrainer();
		String queueName = args[0];
		bean.drainQueue(queueName);
	}
	
	public void drainQueue(String queueName) throws Exception {
		Connection connection = null;
		Context ctx = new InitialContext();
		try {
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Queue queue = (Queue)ctx.lookup(queueName);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			System.out.println("Start to drain queue: " + queueName);
			int count = 0;
			MessageConsumer messageConsumer = session.createConsumer(queue);
			Message msg = null;
			while ((msg = messageConsumer.receive(5000)) != null) {
				String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
				System.out.println("=== msg#" + (count + 1) + " ===");
				System.out.println(msgStr);
				System.out.println("===============================");

				count ++;
			}
			System.out.println(count + " msgs removed from queue: " + queueName);
			
			session.close();
		} finally {
			if (connection != null) {
				connection.close();
			}
			if (ctx != null) {
				ctx.close();
			}
		}
	}

}
