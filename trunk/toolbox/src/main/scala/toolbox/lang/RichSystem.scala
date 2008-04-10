package toolbox.lang;

import java.lang.{ProcessBuilder, Process}
import java.io.{ByteArrayOutputStream}
object RichSystem {
  import deng.toolbox.io.RichStream.copyStream
  def exec(args :String*) :String ={
    //println("Exec " + args.toList)
    val pb = new ProcessBuilder(args.toArray)
    val p = pb.redirectErrorStream(true).start()
    p.waitFor()
    val output = new ByteArrayOutputStream()
    copyStream(p.getInputStream(), output);
    output.toString();
  }
}
