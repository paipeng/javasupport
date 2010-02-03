package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

public class SyncReceive extends ExampleRunner {
	public static void main(String[] args) {
		SyncReceive main = new SyncReceive();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
		Destination queueA = session.createQueue("queueA");
		MessageConsumer consumer = session.createConsumer(queueA);
		connection.start();
		
		int numOfMsg = 1;
		for (int i = 0; i < numOfMsg; i++) {
			Message msg = consumer.receive();
			System.out.print("Received msg#" + (i + 1));
			msg.acknowledge();
			System.out.println(", acknowledged.");
			print(msg);
		}
	}
}
