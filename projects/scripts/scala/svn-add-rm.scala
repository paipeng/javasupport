class StringHelper(strValue : String) {
  //Execute command with user function to process each line of output.
  def exec(func: String => Unit) : Unit = {
    val commands = strValue.split(" ")
    val proc = new ProcessBuilder(commands: _*).redirectErrorStream(true).start();
    val ins = new java.io.BufferedReader(new java.io.InputStreamReader(proc.getInputStream))
    val sb = new StringBuilder
    
    //spin off a thread to read process output.
    val outputReaderThread = new Thread(new Runnable(){
      def run: Unit = {
        var ln: String = null
        while({ln = ins.readLine; ln != null})
          func(ln)
      }
    })
    outputReaderThread.start()
    proc.waitFor
    outputReaderThread.join()    
    ins.close()
    sb.toString
  }
}
implicit def stringHelper(s : String) = new StringHelper(s)

val svnOperation = (op: String, ln: String) => {  
    val fn = ln.split("\\s+")(1)
    val cmd = "svn "+ op + " " + fn
    println(cmd)
    cmd.exec{ println(_) }
}

"svn st".exec{ ln => if(ln.startsWith("?")) svnOperation("add", ln) }  
"svn st".exec{ ln => if(ln.startsWith("!")) svnOperation("rm", ln) }
