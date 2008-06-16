package toolbox.scalasupport

import java.sql.{Connection, Statement, PreparedStatement, ResultSet}
class MysqlVendor(var user:String, var pass:String, var url:String) extends scala.dbc.Vendor {
  def this() = this("root", "", "jdbc:mysql://localhost/mysql")
  def retainedConnections = 1
  def urlProtocolString = "jdbc:mysql:"
  def nativeDriverClass = Class.forName("com.mysql.jdbc.Driver")
  def uri = new java.net.URI(url)
}

