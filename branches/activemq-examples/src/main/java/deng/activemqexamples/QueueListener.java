package deng.activemqexamples;

import java.util.Date;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class QueueListener extends ConnBase implements MessageListener {
	
	public static void main(String[] args) throws Exception {
		//new StartableRunner().runBean(MessageListenerClienter.class.getName());
		
		QueueListener bean = new QueueListener();
		String queueName = args[0];
		bean.setQueueName(queueName);
		StartableRunner.addShutdownHook(bean);
		bean.start();
	}
	
	@Setter
	private String queueName = "queue/ExampleQueue"; 
	
	@Getter
	private int count =0;
	
	private void initializeMessageListener() throws JMSException {
		logger.debug("Creating a consumer with message listener");
		Session sess = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		Queue queue = sess.createQueue(queueName);
		MessageConsumer consumer = sess.createConsumer(queue);
		consumer.setMessageListener(this);
	}
	
	@Override
	public void start() {
		super.start();
		
		try {
			initializeMessageListener();
			connection.start();
			logger.info("JMS connection started.");		
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	
	}
	
	@Override
	public void onMessage(Message msg){
		logger.info("=== msg#" + count + " received on " + new Date() + " ===");
		logger.info(msgToString(msg));
		
		try {
			if (msg instanceof TextMessage) {
				logger.info("TEXT: " + ((TextMessage)msg).getText());
			}
		} catch (JMSException e) {
			throw new RuntimeException(e);
		}
	}
	
	protected String msgToString(Message msg) {
		return ToStringBuilder.reflectionToString(msg, ToStringStyle.MULTI_LINE_STYLE);
	}
}
