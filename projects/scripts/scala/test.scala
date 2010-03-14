import sweet.helper.StreamHelper._
import sweet.helper.FileHelper._
import java.io._
val fi = new FileInputStream("D:/workspace/scripts/scala/test.scala")
val fo = new FileOutputStream("D:/temp/test.scala")
fi.copy(fo)
new File("D:/temp/test.scala").move(new File("D:/temp/test2.scala"))

