"svn st".execute().text.eachLine{ ln ->
	name = ln.split(/\s/)[-1]
	if(ln =~ /^\?/)
		println "svn add $name".execute().text
	else if(ln =~ /^\!/)
		println "svn rm $name".execute().text
}
