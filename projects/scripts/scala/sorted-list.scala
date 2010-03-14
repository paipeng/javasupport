import collection.mutable._

def insertOrderedItem(ls : ListBuffer[Int], a : Int) = {
  val i = ls.findIndexOf(_ >= a)
  if(i == -1) ls += a
  else ls.insert(i, a)
}
  
val ls = new ListBuffer[Int]
ls ++= List(4,7,9)
println(ls)

insertOrderedItem(ls, 5)
println(ls)
insertOrderedItem(ls, 10)
println(ls)
insertOrderedItem(ls, 8)
println(ls)
insertOrderedItem(ls, 1)
println(ls)
