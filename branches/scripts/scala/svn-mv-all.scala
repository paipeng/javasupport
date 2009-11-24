// usage: scala svnmvall.scala test*.txt test
import sweet.helper.StringHelper._

//val argsList = args.toList
//val dest = argsList.last
//val files = argsList.init
val (files, dest) = (args.take(args.length -1), args.last)
//println("Moving " +  List(files, dest))
files.foreach{ fn => 
  val cmd = "svn move " + fn + " " + dest
  println("Executing : " + cmd)
  cmd.exec{ println(_) }
}