package demo.dengz1.jboss.jms;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.After;
import org.junit.Before;

public abstract class AbstractJndiTest {

	protected Context ctx;
	
	@Before
	public void init() {
		initCtx();
	}
	
	@After
	public void destroy() {
		if (ctx != null) {
			try {
				ctx.close();
			} catch (NamingException e) {
				throw new RuntimeException(e);
			}
		}
	}


	protected void initCtx() {
        Properties props = createCtxProps();
        if (!props.containsKey(Context.INITIAL_CONTEXT_FACTORY))
        	props.setProperty(
        			Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
        if (!props.containsKey(Context.PROVIDER_URL))
        	props.setProperty(Context.PROVIDER_URL, "jnp://localhost:2099");
		try {
			ctx = new InitialContext(props);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Properties createCtxProps() {
		return System.getProperties();
	}

	@SuppressWarnings("unchecked")
	protected <T> T doLookup(String name) {
		try {
			return (T)ctx.lookup(name);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
}
