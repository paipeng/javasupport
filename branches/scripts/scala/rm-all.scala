import sweet.helper.FileHelper._
args.foreach{ arg => 
  println("Delete " + arg)
  new java.io.File(arg).deleteAll 
}
