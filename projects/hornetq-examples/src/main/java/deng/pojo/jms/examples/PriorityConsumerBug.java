package deng.pojo.jms.examples;

import java.util.concurrent.CountDownLatch;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.Context;
import javax.naming.InitialContext;

public class PriorityConsumerBug {
	public static void main(String[] args) {
		PriorityConsumerBug main = new PriorityConsumerBug();
		main.run();
	}

	private void run() {
		Connection connection = null;
		try {
			connection = createConnection();
			runExample(connection);			
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			try {
				connection.close();
			} catch (JMSException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void runExample(Connection connection) throws Exception {
		Holder holder = createNewSessionConsumer(connection, 4);
		connection.start();
				
		sendMsgWithNewSession1(connection);
		sendMsgWithNewSession2(connection);
				
		holder.countDownLatch.await(); // wait for consumer to end.
		holder.consumer.close();
		holder.session.close();
		
		System.out.println("Msg received " + holder.msgCount);
	}

	private void sendMsgWithNewSession1(Connection connection) throws Exception {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue exampleQueue = session.createQueue("ExampleQueue");
		MessageProducer producer = session.createProducer(exampleQueue);
		
		Message msg = session.createMessage();
		msg.setStringProperty("color", "RED");
		msg.setStringProperty("myId", "1_1_quicky");
		msg.setIntProperty("expectedProcessTime", 0);
		System.out.println("Sending msg#" + msg.getStringProperty("myId"));	
		producer.send(msg, DeliveryMode.PERSISTENT, 0, 0); 

		msg.setStringProperty("myId", "1_2_busy");
		msg.setIntProperty("expectedProcessTime", 3000);
		System.out.println("Sending msg#" + msg.getStringProperty("myId"));	
		producer.send(msg, DeliveryMode.PERSISTENT, 0, 0); 
								
		producer.close();
		session.close();
	}

	private void sendMsgWithNewSession2(Connection connection) throws Exception {
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue exampleQueue = session.createQueue("ExampleQueue");
		MessageProducer producer = session.createProducer(exampleQueue);
		
		//Thread.sleep(1000);
		
		Message msg = session.createMessage();
		msg.setStringProperty("color", "RED");		
		msg.setStringProperty("myId", "2_1_quicky");
		msg.setIntProperty("expectedProcessTime", 0);				
		System.out.println("Sending msg#" + msg.getStringProperty("myId"));	
		producer.send(msg, DeliveryMode.PERSISTENT, 0, 0); 
		
		msg.setStringProperty("myId", "2_2_high_priority");
		msg.setIntProperty("expectedProcessTime", 0);
		System.out.println("Sending msg#" + msg.getStringProperty("myId"));	
		producer.send(msg, DeliveryMode.PERSISTENT, 9, 0); 
		
		producer.close();
		session.close();
	}
	
	private class Holder {
		MessageConsumer consumer;
		Session session;
		CountDownLatch countDownLatch;
		int msgCount = 0;
	}
	private Holder createNewSessionConsumer(final Connection connection, final int numOfMsgExpected) throws Exception {
		final Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		final Queue exampleQueue = session.createQueue("ExampleQueue");
		final CountDownLatch countDownLatch = new CountDownLatch(numOfMsgExpected);
		MessageConsumer consumer = session.createConsumer(exampleQueue, "color = 'RED'");
		final Holder holder = new Holder();
		holder.consumer = consumer;
		holder.session = session;
		holder.countDownLatch = countDownLatch;
		
		consumer.setMessageListener(new MessageListener() {	
			@Override
			public void onMessage(Message msg) {
				holder.msgCount ++;
				try {
					String myId = msg.getStringProperty("myId");
					String color = msg.getStringProperty("color");
					long expectedProcessTime = msg.getLongProperty("expectedProcessTime");
					System.out.println("Consumer received msg " + myId + ", color=" + color + ", expectedProcessTime=" + expectedProcessTime + " ms.");
					countDownLatch.countDown();
					
					Thread.sleep(expectedProcessTime);
					
					if (holder.msgCount >= numOfMsgExpected) {
						connection.stop();
					}
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		
		return holder;
	}


	private Connection createConnection() throws Exception {
		Context context = null;
		try { 
			context = new InitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory)context.lookup("/ConnectionFactory");
			return connectionFactory.createConnection();
		} finally {
			if (context != null) {
				context.close();
			}
		}
	}
}
