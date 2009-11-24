ls = "svn st".execute().text
ls.eachLine{ ln ->
  println(ln) 
  words = ln.split(/\s+/)
  //println(words)
  if (words[0] == "?") {
    println("adding ${words[1]}")
    "svn add ${words[1]}".execute()
  } else if (words[0] == "!") {
    println("removing ${words[1]}")
    "svn rm ${words[1]}".execute()
  }
}
