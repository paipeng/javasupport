package sweet.helper
object ScriptHelper {
	import collection.jcl.Conversions._
	val testMap = Map("one" -> 1, "two" -> 2, "three" -> 3)
	val testList = List(1, 2, 3, 4, 5)
	def time = System.currentTimeMillis
	def sysprops = System.getProperties
	
	implicit def enu2itr[T](enu: java.util.Enumeration[T]) = {
		new Iterator[T] {
			def hasNext = enu.hasMoreElements
			def next = enu.nextElement
		}
	}	
	implicit def col2wrapper[T](col: java.util.Collection[T]) = {
		new collection.jcl.CollectionWrapper[T]{
			def underlying = col
			def transform(f: (T) => T) : Boolean = false
		}
	}
	implicit def props2helper(props: java.util.Properties) = {
		val underlying = props.asInstanceOf[java.util.Hashtable[String, String]]
		new scala.collection.jcl.Hashtable[String, String](underlying){
			def grep(search: String) = filter{ case (k, v) => k.contains(search) }
		}
	}
	implicit def file2helper(file: java.io.File) = new FileHelper(file)
	implicit def string2helper(str: String) = new StringHelper(str)
	
	implicit def itr2helper[T](itr: scala.Iterable[T]) = {
		new Object{
			def dumpPair = {
				print(itr.getClass + "(\n")
				itr.foreach{ case (k, v) => 
					print("  " + k + " -> " + v + "\n")
				}
				println("\n)")
			}
			def dump = itr match{
				case m: scala.collection.Map[_, _] => dumpPair
				case _ => println(itr.mkString(itr.getClass + "(\n  ", ",\n  ", "\n)"))
			}
			def grep(search: String) = itr match{ 
				case m: scala.collection.Map[_, _] => m.filter{ case (k, v) => k.toString.contains(search) }
				case _ => itr.filter{ x => x.toString.contains(search) }
			}
		}
	}
}
