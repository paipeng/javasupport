// mkcp '/jb/client/*' /jb/common/lib/jbosssx.jar
import org.jboss.resource.security.SecureIdentityLoginModule
println(SecureIdentityLoginModule.encode(args[0]))

