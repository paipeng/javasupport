ls = []
if(args.size()>=1){
  ls = System.properties.keys().findAll{it.contains(args[0])}
}else{
  ls = System.properties.keys()
}
ls.each{ println "$it : ${System.properties[it]}" }
