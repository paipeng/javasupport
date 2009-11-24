def dir = args.size() >= 1 ? args[0] : "."
def zipWithIdx = [args, (0..<args.size()).toList()].transpose()
def nameOpt = zipWithIdx.find{ a, i -> a =~ /-name/ }
if(nameOpt){
	def name = args[nameOpt[1] + 1]
	new File(dir).eachFileRecurse{ if(it =~/$name/) println it }
}else{
	new File(dir).eachFileRecurse{ println it }
}
