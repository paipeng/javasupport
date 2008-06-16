package toolbox.demo

/**
 * Just a empty main for testing.
import sayp.scalasupport.lang._
object Main extends CliApplication {
  def main(args: Array[String]) = {
    val (argList, options) = parseOptions(args) { (argList, options) => {
      case "-file" => options("file") = "foo.bar"
    }}
    val debug = options.contains("debug")
    if(debug) println(argList)
    println("This is sayp tool project.")
    val file = options.getOrElse("file", "test.txt")
    println("Processing fake data value: " + file)
  }
}
*/

