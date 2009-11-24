class Task implements Runnable{
	def void run() {
		10.times{ n ->
			println(Thread.currentThread().getName() + " " + "run #"+ n)
			Thread.sleep(100)
		}
	}
}
a = []
3.times{ a << new Thread(new Task(), "t"+it) }
//a[1].start()
//a[0,2].each{ t -> t.start() }
a.each{ t -> t.start() }
a.each{ t -> t.join() }
println("Done.")