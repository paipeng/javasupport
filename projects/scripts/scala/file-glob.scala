import java.io._
import java.util.regex._

// Convert a unix glob like pattern into Java RE pattern object. Unix file name glob pattern rules (from Ruby's Dir.glob doc) are:
//   *:	Matches any file. Can be restricted by other values in the glob. 
//      * will match all files; 
//      c* will match all files beginning with c; 
//      *c will match all files ending with c; 
//      and c will match all files that have c in them (including at the beginning or end). Equivalent to / .* /x in regexp.
//   ?:	Matches any one character. Equivalent to /.{1}/ in regexp.
//   [set]:	Matches any one character in set. Behaves exactly like character sets in Regexp, including set negation ([^a-z]).
//   {p,q}:	Matches either literal p or literal q. Matching literals may be more than one character in length. 
//      More than two literals may be specified. Equivalent to pattern alternation in regexp.
def globToRegex(globPattern : String) : Pattern = {
  var translate = globPattern
  translate = translate.replaceAll("\\.", "\\\\.")
  translate = translate.replaceAll("\\{", "\\(")
  translate = translate.replaceAll("\\}", "\\)")
  translate = translate.replaceAll("\\,", "\\|")  
  translate = translate.replaceAll("\\?", "\\.{1}")
  translate = translate.replaceAll("\\*", "\\.*?")
  //remove last less greedy flag if there is any.
  if(translate.endsWith("?")) 
    translate = translate.substring(0, translate.length -1)
  
  Pattern.compile(translate)
}
def fnmatch(globPattern : String, fn : String) : Boolean = fnmatch(globToRegex(globPattern), fn)
def fnmatch(rePattern : Pattern, fn : String) : Boolean = rePattern.matcher(fn).matches
// Get a list of java.io.File using unix glob like pattern. If nested path is specified, use ** 
// to denoted matches directories recursively. You MUST use / as path separator!
//   Eg. /tmp/**/*.log
def glob(globPattern : String, dir : File) : List[File] = {
  val paths = globPattern.split("/").toList
  glob(paths.map{ globToRegex(_) }, dir)
}
def glob(rePatterns : List[Pattern], dir : File) : List[File] = {
  if(!dir.isDirectory)
      throw new IllegalArgumentException("Not a directory.")
  
  val subfiles = dir.listFiles match {
    case null => List()
    case files@_ => files.toList
  }
  
  rePatterns match {
    case fnPattern :: Nil => 
      subfiles.filter{ file => fnmatch(fnPattern, file.getName) }
    case dirPattern :: restPatterns =>
      println(List(dirPattern, dir.getName))
      if(fnmatch(dirPattern, dir.getName)){
        subfiles.flatMap{ file => 
          if(file.isDirectory)
            glob(restPatterns, file)
          else
            Nil
        }
      }else{
        Nil
      }
    case _ => Nil
  }
}

//val filenames = List("bar.zip", "ax.txt", "bx.txt", "foo.jar", "test.txt", "test", "bar")
//println(filenames.filter{ fnmatch("*",                _) })
//println(filenames.filter{ fnmatch("*.zip",            _) })
//println(filenames.filter{ fnmatch("*.txt",            _) })
//println(filenames.filter{ fnmatch("*es*",             _) })
//println(filenames.filter{ fnmatch("[ba]*.*",          _) })
//println(filenames.filter{ fnmatch("b*.*",             _) })
//println(filenames.filter{ fnmatch("b*r*",             _) })
//println(filenames.filter{ fnmatch("bar*",             _) })
//println(filenames.filter{ fnmatch("{ax,bx}.txt",      _) })
//println(filenames.filter{ fnmatch("{a,b}x.txt",       _) })
//println(filenames.filter{ fnmatch("??.txt",           _) })
//println(filenames.filter{ fnmatch("*r*",              _) })
//println(filenames.filter{ fnmatch("xxx",              _) })
//println(filenames.filter{ fnmatch("xxx*fff",          _) })
//println(filenames.filter{ fnmatch("x{y,z,x}xx*ff.f?", _) })

println(glob("**/*.*", new File("D:/temp")))

