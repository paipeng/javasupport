package deng.toolbox

/**
 * Create new mysql db with user and perssions.
 */
object CreateMysqlDatabase extends deng.toolbox.lang.CliApplication {
  def main(argv: Array[String]) = {
    val (args, opts) = parseOptions(argv)
  
    if(opts.contains("help") || opts.contains("h")){
      println("usage: scala CreateMysqlDatabase [options] arguments")
      println("[options] --help, -h   Display helpage.")
      println("[options] --dryrun     Do not execute to database.")
      println("[options] --dbUser     DB user that have access to mysql table.")
      println("[options] --dbPass     <empty>.")
      exit
    }
    
    val dbname = if(args.length>=1) args(0) else "mydb"
    val user = if(args.length>=2) args(1) else "devuser"
    val pass = if(args.length>=3) args(2) else "devuser123"
    val dbUser = opts.getOrElse("dbUser", "root")
    val dbPass = opts.getOrElse("dbPass", "")
    
    val stmts = new scala.collection.mutable.ListBuffer[String]()
    stmts += ("create database if not exists " + dbname)
    stmts += ("grant all on "+dbname+".* to '"+user+"'@'localhost' identified by '"+pass+"'")
    stmts += ("flush privileges")
    
    
    if(opts.contains("dryrun")){    
      stmts.foreach(ln=>println(ln+";"))
    }else{
      import deng.toolbox.db.{MysqlVendor}
      val conn = new MysqlVendor(dbUser,dbPass, "jdbc:mysql://localhost/mysql").getConnection
      val stmt = conn.createStatement
      
      stmts.foreach(ln=>{println("Executing: " +ln); stmt.execute(ln)})
      
      stmt.close
      conn.close
    }
  }
}

