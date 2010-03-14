package deng.pojo.jms.examples;

import java.util.Enumeration;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

public class SyncReceive extends ExampleRunner {
	public static void main(String[] args) {
		SyncReceive main = new SyncReceive();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Queue aQueue = session.createQueue("ExampleQueue");
		connection.start();
		
		int numOfMsg = getMsgCount(session, aQueue);
		System.out.println("There are " + numOfMsg + " in " + aQueue);

		MessageConsumer consumer = session.createConsumer(aQueue);
		for (int i = 0; i < numOfMsg; i++) {
			Message msg = consumer.receive();
			System.out.print("Received msg#" + (i + 1));
			msg.acknowledge();
			System.out.println(", acknowledged.");
			print(msg);
		}
	}

	private int getMsgCount(Session session, Queue queue) throws JMSException {
		QueueBrowser qb = session.createBrowser(queue);
		Enumeration<?> en = qb.getEnumeration();
		int i = 0;
		while (en.hasMoreElements()) {
			en.nextElement();
			i ++;
		}
		return i;
	}
}
