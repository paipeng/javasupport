package toolbox.scalasupport
object JMap{
  def apply[A,B](elems : (A, B)*): java.util.Map[A,B] = {
    val jmap = new java.util.HashMap[A,B]
    for((k,v) <- elems) jmap.put(k,v)
    jmap
  }
}

object JArray{
  /** This will allow you do thing like: <pre>String.format("%4d,%s", JArray(4, "foo"))</pre> */
  //def apply(x: Any*) = x.asInstanceOf[scala.runtime.BoxedObjectArray].unbox(x.getClass).asInstanceOf[Array[Object]]
  def apply(x: Any*) = x.map(_.asInstanceOf[AnyRef]).toArray
}

object JCollectionView {  
  /** Convert java.util.Hashtable to scala.collection.jcl.Hashtable[String,String]
  //def foo = new scala.collection.jcl.MapWrapper[String,String](){def underlying=System.getProperties.asInstanceOf[java.util.Map[String,String]]}
  implicit def hashtable2MapWrapper(h: java.util.Hashtable[Object,Object]) = 
    new scala.collection.jcl.Hashtable[String,String](h.asInstanceOf[java.util.Hashtable[String,String]])
  */
  
  /** Convert java.util.Enumeration to EnumerationIterator
   * Example: System.getProperties.keys.filter(_.asInstanceOf[String].contains("java")).toList
   */
  implicit def jenu2enuitr[A](e: java.util.Enumeration[A]) = new EnumerationIterator(e)
}

class EnumerationIterator[A](e : java.util.Enumeration[A]) extends Iterator[A] {
   def hasNext = e.hasMoreElements
   def next() = e.nextElement()
}

