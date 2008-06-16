package toolbox
import toolbox.scalasupport._
import RichSystem._
object Svn extends CliApplication {
  def main(args: Array[String]): Unit = {    
    val (args, opts) = parseOptions(argv){ (args, opts) =>
      case _ =>
    }
		val(subcommand, subargs) = args
    subcommand match {
			case "status" => execWithResult("svn", "status", if(subargs.size==0) "." else subargs(0)){ println(_) }
		}
  }
  
  def usage = "Usage: scala Svn [options] ExtraSubCommand [arg ...]" + """
    |   -h display helpage.                                                 
    |   ExtraSubCommand status|add|remove|checkin|all
    """.stripMargin
}
