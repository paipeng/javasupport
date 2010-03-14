def task = () => (0 to 360 by 15).map{ n => Math.cos(n) }

def timeit(run : () => Unit) = {
  def t = System.currentTimeMillis
  val t1 = t
  run()
  println(t - t1)
}

timeit(() => task)

