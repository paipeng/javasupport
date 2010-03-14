/*
//default paramter with constructors?
class Foo(val s: String, val s2: String) {
  def this(i: Int) = this("" + i, "test")
  override def toString = "Foo(" + s + ", " + s2 + ")"  
}
class Foo2(val s: String = "99", val s2: String = "test") {
  def this(i: Int) = this("" + i, "test2")  
  override def toString = "Foo(" + s + ", " + s2 + ")"  
}
println(new Foo("123", "foo"))
println(new Foo(s2 = "foo2", s="456"))
println(new Foo2())
*/

//Calling parent constructors
class E(s: String, t: Throwable) extends Exception(s, t) {
  def this() = this(null, null)
  def this(s: String) = this(s, null)
}
class E2(s: String = null, t: Throwable = null) extends Exception(s, t)

println(new E().printStackTrace)
println(new E("test").printStackTrace)
println(new E("test", new Exception("foo")).printStackTrace)
println(new E2().printStackTrace)
println(new E2("test2").printStackTrace)
println(new E2("test2", new Exception("foo")).printStackTrace)

