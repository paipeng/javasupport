def loader = this.class.classLoader.rootLoader
loader.addURL(new URL('file:///C:/apps/jboss-5.1.0.GA/common/lib/jbosssa.jarXXX'))
import org.jboss.resource.security.SecureIdentityLoginModule
println(SecureIdentityLoginModule.encode(args[0]))
