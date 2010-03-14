`svn st`.split(/\n/).grep(/^\?/).each do |ln|
	f = ln.split[1]
	puts `svn add #{f}`
end
`svn st`.split(/\n/).grep(/^\!/).each do |ln|
	f = ln.split[1]
	puts `svn rm #{f}`
end
