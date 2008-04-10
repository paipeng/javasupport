package toolbox.jcl

/** Create java HashMap object in Scala syntax. */
object JMap{
  def apply[A,B](elems : (A, B)*): java.util.Map[A,B] = {
    val jmap = new java.util.HashMap[A,B]
    for((k,v) <- elems) jmap.put(k,v)
    jmap
  }
}
