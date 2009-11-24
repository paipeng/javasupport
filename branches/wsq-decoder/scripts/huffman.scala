//Implementing a exercise from
//http://www.siggraph.org/education/materials/HyperGraph/video/mpeg/mpegfaq/huffman_tutorial.html

abstract class BaseNode(var freq: Int)
class Node(freq: Int, var left: BaseNode, var right: BaseNode) extends BaseNode(freq) {
  //override def toString = freq + ":* L=" + left + ", R=" + right
  override def toString = freq + ":*"
}
class Leaf[T](freq: Int, var value: T) extends BaseNode(freq) {
  override def toString = freq + ":" + value
}

def huffmanTree[T](nums: List[Leaf[T]]): Node = {    
  import java.util.PriorityQueue
  import java.util.Comparator
  val comparator = new Comparator[BaseNode] {
    def compare(o1: BaseNode, o2: BaseNode) = o1.freq.compare(o2.freq)
  }
  val queue = new PriorityQueue[BaseNode](nums.size, comparator)
  nums.foreach{ n => queue.add(n) }
  
  def topNode: Node = {
    var n1 = queue.poll
    var n2 = queue.poll
    var top = new Node(n1.freq + n2.freq, n1, n2)
    queue.add(top)    
    if(queue.size == 1)
      top
    else
      topNode
  }
  topNode
}
def printTree(n: BaseNode): Unit = {
  println(n)
  n match {
    case node: Node => {
      printTree(node.left)
      printTree(node.right)
    }
    case _ => 
  }
}

import scala.collection.mutable.HashMap
def codeTable[T](tree: Node): HashMap[T, String] = {
  val map = new HashMap[T, String]
  def walk(n: BaseNode, code: String): Unit = {
    n match {
      case node: Node => {
        walk(node.left, code + "0")
        walk(node.right, code + "1")
      }
      case leaf: Leaf[_] => {
        map.put(leaf.value.asInstanceOf[T], code)
      }
      case _ => throw new RuntimeException("Unknown node type. " + n) 
    }
  }
  walk(tree, "")
  map
}

def encode[T](values: List[T], ctable: HashMap[T, String]): String =
  values.foldLeft(""){ (code, v) => code + ctable(v) }

val table = List(
  new Leaf(5, 1),
  new Leaf(7, 2),
  new Leaf(10, 3),
  new Leaf(15, 4),
  new Leaf(20, 5),
  new Leaf(45, 6))

val tree = huffmanTree(table)
//printTree(tree)
val ct = codeTable[Int](tree)
val code = encode(List(1,2,5,5,4,2,3,2,6), ct)
println(code)
