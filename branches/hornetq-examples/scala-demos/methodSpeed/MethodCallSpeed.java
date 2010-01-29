public class MethodCallSpeed {
	public static void doWork() { }
	public static void main(String[] args) {
		int repeat = 10
		int n = 999999999
		if(args.length >= 1) {
			n = Integer.parseInt(args[0])
		}
			
		(1 to repeat).foreach { i =>
			var i = 0
			val started = ts
			while (i < n) { 
				i += 1
				doWork()
			}
			val stopped = ts
			val elapse = stopped - started
			val rate = n / (elapse / 1000.0)
			printf("Method call rate %.2f calls/sec\n", rate)
		}
	}
}
