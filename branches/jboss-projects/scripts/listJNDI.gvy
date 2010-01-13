import javax.naming.*

def ctx = new InitialContext()
def cf = ctx.doLookup("ConnectionFactory")
println(cf)

