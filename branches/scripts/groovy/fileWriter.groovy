new File("D:/temp/file-poller/test.txt").withWriter{ writer ->
	writer.write("stamped " + new Date()+"\n")
	writer.write("stamped " + new Date()+"\n")
	writer.write("stamped " + new Date()+"\n")
	writer.write("stamped " + new Date()+"\n")
}