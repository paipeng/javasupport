/*
Search filename in a parent directory. Without options, it simply search filename
ignore case. With -r option, it will find filename that match the Java RegEx pattern.

To ignore case with pattern, do:
scala find.scala -r d:\ "(?i)myfile"
*/
import java.io._
import sweet.helper.FileHelper._

val (opts, args) = argv.partition{ _.startsWith("-") }
val reOpt = opts.find{ _ == "-r" }
val sizeOpt = opts.find{ _ == "-s" } != None
val dir = new File(args(0))
val text = if(args.length != 2) "" else args(1).toLowerCase

val finder = reOpt match {
  case Some(re) =>
    (fn : String) => java.util.regex.Pattern.compile(text).matcher(fn).find
  case None => 
    (fn : String) => fn.toLowerCase.contains(text)
}

dir.foreachFile{ file =>
  if(finder(file.getName)){
    if(sizeOpt)
      printf("%12s ", file.length) 
    println(file.getAbsolutePath)
  }
    
}