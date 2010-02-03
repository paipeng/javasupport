package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

public class AsyncSend extends ExampleRunner {
	public static void main(String[] args) {
		AsyncSend main = new AsyncSend();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Destination queueA = session.createQueue("queueA");
		MessageProducer producer = session.createProducer(queueA);
		Message msg = session.createMessage();
		
		int numOfMsg = 1;
		for (int i = 0; i < numOfMsg; i++) {
			System.out.print("Sedning msg#" + (i + 1));
			producer.send(msg);
			System.out.println(".");
		}
	}
}
