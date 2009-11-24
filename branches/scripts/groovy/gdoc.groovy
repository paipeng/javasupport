/* Date: 10/6/2007
 * Author: thebugslayer@gmail.com
 * This script can lookup both Java and Groovy's meta methods 
 * and quickly display the method signatures.
 * The output is 
 *   methodName : (parameterTypes) => returnType.
 */

if(args.size()<1){
	println "usage ${this.class.name} className -- display all public methods signatures."
	println "usage ${this.class.name} package.className -- same as above with full package prefix."
	println "usage ${this.class.name} className methodName -- display only methods that match this name."
	System.exit(1)
}
	
def className = args[0]
def methodName = args.size()>1 ? args[1] : null
def defaultJavaPackageNames = [
	'java.lang',
	'java.util',
	'java.io',
	'java.net'
]

def displayGDoc(clz, filterMethodName=null){
	def mdocs = [:]
	def addMethodsToMDocs = { m -> 
		ary = mdocs.get(m.name, [])
		ary << m.name << m.parameterTypes.collect{it.name}.join(", ") << m.returnType.name
		mdocs[m.name] = ary		
	}
	def displayMDocEntry = { k,v -> 
		printf("%-25s : (%s) => %s\n", v)
	}
	def metaClz = GroovySystem.metaClassRegistry.getMetaClass(clz)
	metaClz.methods.each{ addMethodsToMDocs(it) }
	metaClz.metaMethods.each{ addMethodsToMDocs(it) }
	clz.methods.each{ addMethodsToMDocs(it) }
	
	if(filterMethodName!=null){
		def mdocsDisplay = [:]
		//accept only ones that matches.
		mdocs.each{ k, v -> 
			if(k.indexOf(filterMethodName)>=0)
				mdocsDisplay[k] = v
		}
		if(mdocsDisplay.size() <= 0 ){
			println "$filterMethodName not found in $clz"
		}else{
			mdocsDisplay.keySet().sort().each{ k -> 
				v = mdocsDisplay[k]
				displayMDocEntry(k, v)
			}
		}
	}else{
		mdocs.keySet().sort().each{ k -> 
			v = mdocs[k]
			displayMDocEntry(k, v)
		}
	}
}

if(className.indexOf('.')>0){
	//prefix of package is given
	displayGDoc(Class.forName(className), methodName)
	System.exit(0)
}else{
	//prefix of package is missing, try to find it
	for(prefix in defaultJavaPackageNames){
		try{
			displayGDoc(Class.forName("${prefix}.${className}"), methodName)
			System.exit(0)
		}catch(ClassNotFoundException e){
			//do nothing.
		}
	}
	println "class ${className} not found."
}
