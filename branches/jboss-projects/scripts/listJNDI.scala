// $ scala -cp $CLASSPATH listJNDI.scala

import javax.naming._

// Listing a context names binding
def list(ctx: Context) {
	println()
	println("Namespace: " + ctx.getNameInNamespace)
	//names sorted by className, then binding name. The element is a NameClassPair
	val javaEnumerator = ctx.list("")
	val names = new Iterator[NameClassPair] {
		def hasNext = javaEnumerator.hasMore
		def next = javaEnumerator.next
	}.toList
	names.sort { (a, b) => 
		var ret = a.getClassName == b.getClassName
		if (ret == true) {
			ret = a.getName == b.getName
		}
		ret
	}
	names.foreach { nameClassPair =>
		if (nameClassPair.getClassName.contains("org.jnp.interfaces.NamingContext")) {
			list(ctx.lookup(nameClassPair.getName).asInstanceOf[Context])
		} else {
			println(nameClassPair.getName + " : " + nameClassPair.getClassName)
		}
	}
}
list(new InitialContext())
