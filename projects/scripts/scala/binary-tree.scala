class Node[T](val value: T, val freq : Int) {
  def this(value: T, freq : Int, left : Node[T], right : Node[T]){
    this(value, freq)
    this.left = left
    this.right = right
  }
  var left : Node[T] = null
  var right : Node[T] = null
  override def toString = "Node(" + value + ", " + freq + ")"
}

val nodeComparator = (node : Node[String]) => new Ordered[Node[String]]{
  def compare(that : Node[String]) : Int = {
    if(node.freq == that.freq) 0
    else if(node.freq > that.freq) 1
    else -1
  }  
}
val queue = new collection.mutable.PriorityQueue[Node[String]]()(nodeComparator)

val left = new Node("C", 5)
val right = new Node("B", 4)
val root = new Node("A", 2, left, right)
queue += (root, left, right)

println(root)
println(queue)
