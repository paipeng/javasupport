package demo.dengz1.cameldemo;

import java.util.Date;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.JndiRegistry;
import org.apache.camel.spi.Registry;

public class CamelContextDemo {
	
	/**
	 * Registry demo.
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		CamelContext camelCtx = new DefaultCamelContext();
		Registry registry = camelCtx.getRegistry();
		System.out.println(registry.getClass());
		
		Object bean = registry.lookup("");
		System.out.println(bean);
		
		JndiRegistry jndiRegistry = (JndiRegistry)registry;		
		System.out.println(jndiRegistry.getContext().getClass());
		
		jndiRegistry.bind("d1", new Date());
		
		System.out.println(registry.lookup("d1"));
	}
}
