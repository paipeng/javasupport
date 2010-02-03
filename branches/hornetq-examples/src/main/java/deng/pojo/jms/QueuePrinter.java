package deng.pojo.jms;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueuePrinter {
	
	public static void main(String[] args) throws Exception {
		QueuePrinter bean = new QueuePrinter();
		String queueName = args[0];
		bean.browseQueue(queueName);
	}
	
	public void browseQueue(String queueName) throws Exception {
		Connection connection = null;
		Context ctx = new InitialContext();
		try {
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
