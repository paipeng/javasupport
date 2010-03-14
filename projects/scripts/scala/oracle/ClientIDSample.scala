import java.sql.Connection
import java.sql.SQLException
import java.sql.Statement
 
import javax.sql.DataSource
 
import oracle.jdbc.internal.OracleConnection
import oracle.jdbc.pool.OracleDataSource
 
object ClientIDSample {
     def main(args: Array[String]) {
           val connn = getConn();
           val stmt = connn.createStatement();
           stmt.execute("select count(*) from all_objects");
           connn.close();
     }
 
     def getConn(): Connection = {
           val oracleDS = new OracleDataSource();
           oracleDS.setUser("scott");
           oracleDS.setPassword("tiger");
           oracleDS.setURL("jdbc:oracle:thin:@localhost:1521:mydb");         
           val ds = oracleDS
           val conn = ds.getConnection();
           val metrics = new Array[String](oracle.jdbc.OracleConnection.END_TO_END_STATE_INDEX_MAX);
           metrics(oracle.jdbc.OracleConnection.END_TO_END_ACTION_INDEX) = "MY_PROJ";
           metrics(oracle.jdbc.OracleConnection.END_TO_END_MODULE_INDEX) = "MY_MODULE";
           metrics(oracle.jdbc.OracleConnection.END_TO_END_CLIENTID_INDEX) = "MY_CLIENT_ID";
           conn.asInstanceOf[oracle.jdbc.OracleConnection].setEndToEndMetrics(metrics, 0);
           conn;
     } 
}
