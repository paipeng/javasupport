//In groovy, every thing is public.
//All object members from Groovy/Java are accessable, even if you are use private access modifier!
class A{
  private def x = 123
}
println(new A().x)

def r = new BufferedInputStream(new FileInputStream("C://test.txt"))
println(r.count) //Access protected member!
println(r.read())
println(r.count)
println(r.buf.length)
r.close()
