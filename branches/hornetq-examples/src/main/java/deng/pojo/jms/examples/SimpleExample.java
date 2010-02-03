package deng.pojo.jms.examples;

import javax.jms.Connection;
import javax.jms.JMSException;

public class SimpleExample extends ExampleRunner {
	public static void main(String[] args) {
		SimpleExample main = new SimpleExample();
		main.run();
	}

	@Override
	protected void runExample(Connection connection) throws JMSException {
		System.out.println("Got connection " + connection);
	}
}
