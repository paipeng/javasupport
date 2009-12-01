package demo.dengz1.jboss.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

import org.junit.Test;

public class JndiNamesListTest {
	protected Context jndiContext;
	
	@Test
	public void getConnectionFactory() throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		props.setProperty(Context.PROVIDER_URL, "jnp://localhost:2099");
        jndiContext = new InitialContext(props);

		System.out.println("JNDI namespace: global");
        dumpContext(jndiContext);
	}

	private void dumpContext(Context ctx) throws Exception {	
		String namespace = ctx.getNameInNamespace();
		NamingEnumeration<NameClassPair> names = ctx.list("");

		while(names.hasMoreElements()) {
			NameClassPair ncp = (NameClassPair)names.nextElement();
			String name = ncp.getName();
			String clsName = ncp.getClassName();
			System.out.printf("  %-40s %s\n", name, clsName);
			
			if (clsName.equals("org.jnp.interfaces.NamingContext")) {
				String nextNamespace = namespace + "/" + name;
				System.out.println("JNDI namespace: " + nextNamespace);
				dumpContext((Context)ctx.lookup(nextNamespace));
			}
		}
	}
}
