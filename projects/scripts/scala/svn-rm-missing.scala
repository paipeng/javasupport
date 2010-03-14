import sweet.helper.StringHelper._
val files = "svn st".execResultAsList.filter{ _.startsWith("!") }.map{ _.split("\\s+")(1) }
if(files.size > 0){
  val cmd = "svn remove " + files.mkString(" ")
  println("Executing : " + cmd)
  cmd.exec{ println(_) }
}
