//import scala.collection.mutable._
//type T2 = Tuple2[String, Int]
//implicit def qcmp(x : T2) : Ordered[T2] = { 
//    new Ordered[T2] {
//      def compare(that: T2) : Int = x._2.compare(that._2)
//    }
//}
//val q = new PriorityQueue[T2]
//q += (("c", 1))
//q += (("a", 3))
//q += (("e", 2))
//println(q)

//import scala.collection.mutable._
//import scala.runtime.RichString
//implicit def qcmp(x : RichString) : Ordered[RichString] = { 
//    new Ordered[RichString] {
//      def compare(that: RichString) : Int = -(x.compare(that))
//    }
//}
//val q = new PriorityQueue[RichString]
//q += ("c", "a", "e")
//println(q)


//def qcmp(x : String) : Ordered[String] = { 
//    new Ordered[String] {
//      def compare(that: String) : Int = -(x.compare(that))
//    }                                       
//}
//def test(ls : List[String])(implicit cmp : String => Ordered[String]) = {
//  ls.sort{ (a, b) => cmp(a) >= b }.foreach{ println(_) }
//}
//test(List("c", "a", "e"))(qcmp)


//WILL FAIL!!!
//import scala.collection.mutable._
//implicit def qcmp(x : String) : Ordered[String] = { 
//    new Ordered[String] {
//      def compare(that: String) : Int = -(x.compare(that))
//    }                                       
//}
//val q = new PriorityQueue[String]
//q += ("c", "a", "e")                                                
//println(q)                                                          

//TO FIX THIS, YOU MUST NOT DECLARE IMPLICIT!
import scala.collection.mutable._
def qcmp(x : String) : Ordered[String] = { 
    new Ordered[String] {
      def compare(that: String) : Int = -(x.compare(that))
    }                                       
}
val q = new PriorityQueue[String]()(qcmp) //notice it's the second param!
q += ("c", "a", "e")                                                
println(q)   

