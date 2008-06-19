package toolbox
import toolbox.scalasupport._
import RichSystem._
object Svn extends CliApplication {
  def usage = """CLI extenstion to Svn client that add, remove and checkin pending files quickly based
    | on a working dir status.
    |
    | Usage: scala Svn [options] st|add|rm|ci|all [WorkingDir]
    |   -h display helpage.                               
    | 
    | default WorkingDir is cwd.
    """.stripMargin
    
  def main(argv: Array[String]){    
    val (args, opts) = parseOptions(argv)    
    if(opts.contains("-h") || args.size<1) exitWith(usage)
      
		val (subcommand::subargs) = args
    def workingdir = if(subargs.size==0) "." else subargs(0)
    def getPendingFiles = execWithResult("svn", "status", workingdir) _ 
    def add = getPendingFiles{ ln =>
          val Array(flag, file) = ln.split("\\s+")
          if(flag == "?")
            println(exec("svn", "add", file))
      }
    def rm = getPendingFiles{ ln =>
        val Array(flag, file) = ln.split("\\s+")
        if(flag == "!")
          println(exec("svn", "rm", file))
      }
    def ci = println("Commiting files...\n"+exec("svn", "ci", "-m", "Auto checkin.", workingdir))
    
    subcommand match {
			case "st" => getPendingFiles { println(_) }
			case "add" => add
      case "rm" => rm
      case "ci" =>  ci   
      case "all" => { add; rm; ci }
      case _ => exitWith("Wrong argument.")
		}
  }
}
