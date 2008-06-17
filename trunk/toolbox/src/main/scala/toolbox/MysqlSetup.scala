package toolbox

object MysqlSetup extends toolbox.scalasupport.CliApplication {
  def main(argv: Array[String]) = {
    val (args, opts) = parseOptions(argv)
    if(opts.contains("h") || args.size < 1){
      exitWith(
      """Find file by name or list them all from a dir.
        | usage: Mysql [options] new_dbname [new_user] [new_pwd]
        | [options] -h       Display helpage.
        | [options] -n       No action. Dry run only.
        | [options] -uUSER   db user used to create new db and etc. default root.
        | [options] -pPWD    db user password. default empty.
        | [options] -rURL    db connection url. default to localhost/mysql db.
        |
        | Default [new_user] is devuser
        | Default [new_pwd] is [new_user]123
      """.stripMargin)
    }
    
    val dbname = args(0)
    val user = if(args.length>=2) args(1) else "devuser"
    val pass = if(args.length>=3) args(2) else user+"123"
    val dbUser = opts.getOrElse("u", "root")
    val dbPass = opts.getOrElse("p", "")
    val dbUrl = opts.getOrElse("r", "jdbc:mysql://localhost/mysql")
    val dryrun = opts.contains("n")
    
    val stmts = new scala.collection.mutable.ListBuffer[String]()
    stmts += ("create database if not exists " + dbname)
    stmts += ("grant all on "+dbname+".* to '"+user+"'@'localhost' identified by '"+pass+"'")
    stmts += ("flush privileges")
    
    
    if(dryrun){    
      stmts.foreach(ln=>println(ln+";"))
    }else{
      val vendor = new scala.dbc.Vendor {
        def user = dbUser
        def pass = dbPass
        def uri = new java.net.URI(dbUrl)
        def retainedConnections = 1
        def urlProtocolString = "jdbc:mysql:"
        def nativeDriverClass = Class.forName("com.mysql.jdbc.Driver")
      }
      val conn = vendor.getConnection
      try{
        val stmt = conn.createStatement
        try{
          stmts.foreach(ln=>{println("Executing: " +ln); stmt.execute(ln)})
          println("Done.")
        }finally{ stmt.close }
      }finally{ conn.close }
    }
  }
}

