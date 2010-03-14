package deng.jbossexamples.jndi;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;

/**
 * Example of JBoss 5.1.0GA default server :
 * 
Zemian@Zemian-PC /source/javasupport/branches/jboss-projects/jboss-examples
$ javacp deng.jbossexamples.JndiListBindings
JNDI namespace: global
  UserTransactionSessionFactory            $Proxy224
  UUIDKeyGeneratorFactory                  org.jboss.ejb.plugins.keygenerator.uuid.UUIDKeyGeneratorFactory
  SecureManagementView                     org.jnp.interfaces.NamingContext
JNDI namespace: /SecureManagementView
  remote-org.jboss.deployers.spi.management.ManagementView Proxy for: org.jboss.deployers.spi.management.ManagementView
  remote                                   Proxy for: org.jboss.deployers.spi.management.ManagementView
  SecureDeploymentManager                  org.jnp.interfaces.NamingContext
JNDI namespace: /SecureDeploymentManager
  remote-org.jboss.deployers.spi.management.deploy.DeploymentManager Proxy for: org.jboss.deployers.spi.management.deploy.DeploymentManager
  remote                                   Proxy for: org.jboss.deployers.spi.management.deploy.DeploymentManager
  HiLoKeyGeneratorFactory                  org.jboss.ejb.plugins.keygenerator.hilo.HiLoKeyGeneratorFactory
  XAConnectionFactory                      org.jboss.jms.client.JBossConnectionFactory
  topic                                    org.jnp.interfaces.NamingContext
JNDI namespace: /topic
  ClusteredConnectionFactory               org.jboss.jms.client.JBossConnectionFactory
  ProfileService                           org.jboss.aop.generatedproxies.AOPProxy$2
  SecureProfileService                     org.jnp.interfaces.NamingContext
JNDI namespace: /SecureProfileService
  remote                                   Proxy for: org.jboss.profileservice.spi.ProfileService
  remote-org.jboss.profileservice.spi.ProfileService Proxy for: org.jboss.profileservice.spi.ProfileService
  queue                                    org.jnp.interfaces.NamingContext
JNDI namespace: /queue
  DLQ                                      org.jboss.jms.destination.JBossQueue
  ExpiryQueue                              org.jboss.jms.destination.JBossQueue
  ClusteredXAConnectionFactory             org.jboss.jms.client.JBossConnectionFactory
  UserTransaction                          org.jboss.tm.usertx.client.ClientUserTransaction
  ConnectionFactory                        org.jboss.jms.client.JBossConnectionFactory
  jmx                                      org.jnp.interfaces.NamingContext
JNDI namespace: /jmx
  invoker                                  org.jnp.interfaces.NamingContext
JNDI namespace: /jmx/invoker
  RMIAdaptor                               $Proxy217
  rmi                                      org.jnp.interfaces.NamingContext
JNDI namespace: /jmx/rmi
  RMIAdaptor                               javax.naming.LinkRef
  TomcatAuthenticators                     java.util.Properties
  console                                  org.jnp.interfaces.NamingContext
JNDI namespace: /console
  PluginManager                            $Proxy218


 * @author Zemian
 *
 */
public class ListBindings {
	protected Context jndiContext;
	
	public static void main(String[] args) throws Exception {
		new ListBindings().run();
	}
	
	public void run() throws Exception {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
		props.setProperty(Context.PROVIDER_URL, "jnp://localhost:1099");
        jndiContext = new InitialContext(props);

		System.out.println("JNDI namespace: global");
        listBindings(jndiContext);
	}

	private void listBindings(Context ctx) throws Exception {	
		String namespace = ctx.getNameInNamespace();
		if (!namespace.startsWith("/") && namespace.length() > 1) {
			namespace = "/" + namespace;
		}
		NamingEnumeration<NameClassPair> names = ctx.list("");

		while(names.hasMoreElements()) {
			NameClassPair ncp = (NameClassPair)names.nextElement();
			String name = ncp.getName();
			String clsName = ncp.getClassName();
			System.out.printf("  %-40s %s\n", name, clsName);
			
			if (clsName.equals("org.jnp.interfaces.NamingContext")) {
				String nextNamespace = namespace + "/" + name;
				System.out.println("JNDI namespace: " + nextNamespace);
				listBindings((Context)ctx.lookup(nextNamespace));
			}
		}
	}
}
