package deng.toolbox

object SvnHelper extends deng.toolbox.lang.CliApplication {
  def main(argv: Array[String]): Unit = {    
    val (args, opts) = parseOptions(argv)
    
    if(opts.contains("help") || opts.contains("h")){
      println("usage: scala SvnHelper [options] <subcommand> <workingdir> <subargs>")
      println("[options] --help, -h   Display helpage.")
      exit
    }
		
		val List(subcommand, workingdir, subargs@_*) = args
    subcommand match {
			case "rmall" => removePendingFiles(workingdir)
		}
  }
	
	def removePendingFiles(wd: String): Unit = {
    import deng.toolbox.lang.RichSystem.exec
		val res = exec("svn", "st", wd);
		for(ln <- res.split("\n")){
			val files = ln.split("\\s+");
			if("!".equals(files(0)))
				printf("%s", exec("svn", "rm", files(1)));
		}
	}
}
