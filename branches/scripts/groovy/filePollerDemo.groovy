def rand(min, max){ min + new Random().nextInt(max - min + 1) }
	
//simulate writing a big file that takes few sometime to write out to disk.
def writeBigFile(fn){
	new File(fn).withWriter{ writer ->
		5.times{ 
			n = rand(500, 1000)
			writer.write("stamped " + new Date() + " next n " + n + "\n")
			writer.flush()
			Thread.sleep(n)
		}
	}
}
def pollDir(dir){
	new File(dir).eachFile{ file ->
		println("Got file " + file.text)
	}
}

testfn = "D:/temp/file-poller/test.txt"
poller = Thread.start{ 
	new File(testfn).delete()
	println("Begin file write")
	writeBigFile(testfn)	
	println("Done file write")
}
reader = Thread.start{
	println("Begin file read")
	try{
		while(true){
			pollDir("D:/temp/file-poller")
			Thread.sleep(500)
		}
	}catch(e){
		//suppress.
	}
	println("Done file read")
	//new File(testfn).delete()
}

poller.join()
Thread.sleep(2000)
reader.interrupt()

