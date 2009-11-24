/*def meth(ary : Array[Object]) = ary.foreach{ x=> println(x.getClass) }
meth(Array("a", "b", "c")) //this works! - incosistent.

val a = Array("a", "b", "c")
meth(a) //this fails!


//Scala's List is covariant.
def meth2(ls : List[Object]) = ls.foreach{ x=> println(x) }
meth2(List("a", "b", "c"))
val b = List("a", "b", "c")
meth2(b)

def meth3(ls : List[Any]) = ls.foreach{ x=> println(x) }
val c = List(1,2,3)
meth3(c)

def meth4[T](ary : Array[T]) = ary.foreach{ x=> println(x) }
val d = Array(1,2,3)
meth4(d)

object covariant extends Application {
  def meth(ary: Array[Object]) = ary.foreach{ x=> println(x.getClass) }
  //meth(Array("a", "b", "c"))
  meth(Array(1,2,3))
}

*/

def ary(obj: Object) = println(obj.getClass)
ary(Array(1,2,3))
ary(Array("a", "b", "c"))
val a = Array("a", "b", "c")
ary(a)

// scala> ary(Array(1,2,3))
// class [I
// 
// scala> ary(Array("a", "b", "c"))
// class scala.runtime.BoxedAnyArray
// 
// scala> val a = Array("a", "b", "c")
// a: Array[java.lang.String] = Array(a, b, c)
// 
// scala> ary(a)
// class [Ljava.lang.String;

//Java's ArrayList is invariant.
import scala.collection.jcl.Conversions._
def meth5(ls : java.util.ArrayList[Object]) = ls.foreach{ x=> println(x) }
val b = new java.util.ArrayList[String]
b.add("a")
b.add("b")
b.add("c")
meth5(b)

