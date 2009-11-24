package sweet.helper
class StringHelper(strValue : String) {
  //Execute command with user function to process each line of output.
  def exec(func : String=>Unit) : Unit = {
    val commands = strValue.split(" ")
    val proc = new ProcessBuilder(commands: _*).redirectErrorStream(true).start();
    val ins = new java.io.BufferedReader(new java.io.InputStreamReader(proc.getInputStream))
    val sb = new StringBuilder
    
    //spin off a thread to read process output.
    val outputReaderThread = new Thread(new Runnable(){
      def run : Unit = {
        var ln : String = null
        while({ln = ins.readLine; ln != null})
          func(ln)
      }
    })
    outputReaderThread.start()
    
    //suspense this main thread until sub process is done.
    proc.waitFor
    
    //wait until output is fully read/completed.
    outputReaderThread.join()
    
    ins.close()
  
    //return result.	
    sb.toString
  }
  
  //Execute command with list of output string
  def execResultAsList : List[String] = {
    var ls : List[String] = Nil
    exec{ ln => ls = ln :: ls }
    ls
  }
}

object StringHelper{
  implicit def stringHelper(s : String) = new StringHelper(s) 
}
