package toolbox.scalasupport

import toolbox.scalasupport.RichStream.copyStream
import java.lang.{ProcessBuilder, Process}
import java.io.{ByteArrayOutputStream}

object RichSystem {
  def exec(args: String*) :String = {
    //println("Exec " + args.toList)
    val pb = new ProcessBuilder(args.toArray)
    val p = pb.redirectErrorStream(true).start()
    p.waitFor()
    val output = new ByteArrayOutputStream()
    copyStream(p.getInputStream(), output);
    output.toString();
  }
  def execWithResult(args: String*)(f: String=>Unit){
    exec(args: _*).split("\n").foreach{ ln => f(ln) }
  }
  def props = System.getProperties
   
  def exitWith(msg: String) {
    exitWith(msg, 0)
  }
  def exitWith(msg: String, code: Int) {
    println(msg)
    System.exit(code)
  }
}
