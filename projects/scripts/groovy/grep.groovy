//Grep text from STDIN or one or more files
// usage: C:> dir | groovy grep.groovy bytes
// usage: C:> groovy grep.groovy ERROR *.log
// @author Zemian Deng (dengz1) Jan 13, 2008

grepText = args[0]
def process(ln){ 
	if(ln.contains(grepText)){ println "* ${ln}" }
}
if(args.length >1){
	//perform from files
	args[1..-1].each{ fn -> 
		new File(fn).eachLine{ ln -> process(ln) }
	}
}else{
	//perform from STDIN
	System.in.eachLine{ ln -> process(ln) }
}