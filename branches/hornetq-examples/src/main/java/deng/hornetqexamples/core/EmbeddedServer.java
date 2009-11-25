package deng.hornetqexamples.core;

import org.hornetq.core.config.impl.FileConfiguration;
import org.hornetq.core.server.HornetQ;
import org.hornetq.core.server.HornetQServer;
import org.hornetq.jms.server.JMSServerManager;
import org.hornetq.jms.server.impl.JMSServerManagerImpl;

/**
 * This example is based from blog entry here:
 * http://hornetq.blogspot.com/2009/09/hornetq-simple-example-using-maven.html
 * 
 * @author zemian
 * 
 */
public class EmbeddedServer {
	public static void main(String[] args) throws Exception {
		String configDir = args[0];
		try {
			FileConfiguration configuration = new FileConfiguration();
			configuration.setConfigurationUrl(configDir + "/hornetq-configuration.xml");
			configuration.start();

			HornetQServer server = HornetQ.newHornetQServer(configuration);
			JMSServerManager jmsServerManager = new JMSServerManagerImpl(server, configDir + "/hornetq-jms.xml");
			// if you want to use JNDI, simple inject a context here or don't
			// call this method and make sure the JNDI parameters are set.
			jmsServerManager.setContext(null);
			jmsServerManager.start();
			System.out.println("STARTED::");
		} catch (Throwable e) {
			System.out.println("FAILED::");
			e.printStackTrace();
		}
	}
}
