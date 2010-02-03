package deng.pojo.jms;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueuePrinter {
	
	public static void main(String[] args) throws Exception {
		QueuePrinter main = new QueuePrinter();
		main.queueName = System.getProperty("queueName", "ExampleQueue");
		main.run();
	}
	
	private String queueName;
	public void run() {
		Connection connection = null;
		Context ctx = null;
		try {
			ctx = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory)ctx.lookup("/ConnectionFactory");
			connection = cf.createConnection();
			Queue queue = (Queue)ctx.lookup(queueName);
			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			System.out.println("Start to browse queue: " + queueName);
			int count = 0;
			QueueBrowser browser = session.createBrowser(queue);
			Enumeration<?> en = browser.getEnumeration();
			while (en.hasMoreElements()) {
				Message msg = (Message)en.nextElement();
				String msgStr = ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
				System.out.println("=== msg#" + (count + 1) + " ===");
				System.out.println(msgStr);
				System.out.println("===============================");

				count ++;
			}
			System.out.println(count + " msgs printed from queue: " + queueName);
			
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
