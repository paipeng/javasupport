// mkcp '/jb/client/*' /jb/common/lib/jbosssx.jar
import org.jboss.resource.security.SecureIdentityLoginModule
println(SecureIdentityLoginModule.decode(args[0]))

