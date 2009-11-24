import java.io._
import sweet.helper.FileHelper._

//new File("D:/temp").foreachDir{ dir => println(dir) }
//new File("D:/workspace/scripts").foreachDir{ dir => if(dir.getName == ".svn")println(dir) }
//new File("D:/workspace/scripts").walk{ paths => println(paths) }
//new File("D:/workspace/scripts/test.scala").walk{ paths => println(paths) }
//new File("D:/workspace/scripts").subfiles.foreach{ paths => println(paths) }\
//new File("D:/temp/wiki.bak_jamwiki").deleteAll
new File("D:/temp/test.txt").write("""one
two
three
""")
println(new File("D:/temp/test.txt").read)
new File("D:/temp/test.txt").foreachLine{ ln => println(ln.length) }
new File("D:/temp/test.txt").deleteAll
