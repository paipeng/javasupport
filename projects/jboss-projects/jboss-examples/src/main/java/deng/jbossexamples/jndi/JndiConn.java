package deng.jbossexamples.jndi;

import java.util.Properties;

import javax.jms.ConnectionFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Provide method to init and destroy jndi context field.
 * @author Zemian
 *
 */
public class JndiConn {
	public static void main(String[] args) {
		JndiConn jndi = new JndiConn();
		
		jndi.init();
		
		ConnectionFactory cf = (ConnectionFactory)jndi.doLookup("/ConnectionFactory");
		System.out.println(cf);
		
		jndi.destroy();
	}
	
	protected Context ctx;
	
	protected void destroy() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
	}

	protected void init() {
		Properties props = System.getProperties();
		if (!props.containsKey(Context.INITIAL_CONTEXT_FACTORY))
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		if (!props.containsKey(Context.PROVIDER_URL))
			props.setProperty(Context.PROVIDER_URL, "jnp://localhost:1099");
		try {
			ctx = new InitialContext(props);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	protected <T> T doLookup(String name) {
		try {
			return (T) ctx.lookup(name);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}
