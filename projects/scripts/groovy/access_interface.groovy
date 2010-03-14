//Error: Caught: java.lang.NullPointerException ???
//class Task extends Runnable{
//	def run(){
//		println "Running."
//	}
//}
//t = new Thread(new Task())
//t.start()

// A closure might able coerce into a Java interface. eg:
task = { println "Running#1" } as Runnable
t = new Thread(task)
t.start()

// A closure might able coerce into a Java interface. eg:
t2 = new Thread({ println "Running#2" } as Runnable)
t2.start()

// Using a map and coerce into an interface
impl = [
  i: 10,
  hasNext: { impl.i > 0 },
  next: { impl.i-- }
]
iter = impl as Iterator
println(iter instanceof Runnable)
println(iter instanceof Iterator)
while(iter.hasNext())
	println iter.next()

