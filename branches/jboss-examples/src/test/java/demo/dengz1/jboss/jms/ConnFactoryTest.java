package demo.dengz1.jboss.jms;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;

import org.junit.Test;

public class ConnFactoryTest {
	protected Context ctx;
	
	@Test
	public void getConnectionFactory() throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		props.setProperty(Context.PROVIDER_URL, "jnp://localhost:2099");
        ctx = new InitialContext(props);
        
        ConnectionFactory cf = (ConnectionFactory)ctx.lookup("ConnectionFactory");
        System.out.println(cf);
        
        Destination queue = (Destination)ctx.lookup("queue/A");
        System.out.println(queue);
	}
}
