package test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import jragonsoft.javautil.util.JDBCUtils;

/**
 *  This test requires MySql Database server running.
 *
 *@author     zemian
 *@version    $Id: RunJDBCUtils.java 4 2006-03-16 15:27:19Z zemian $
 */
public class RunJDBCUtils {
	static MysqlDataSource dataSource;
	static String query = "select version()";
	static String url = "jdbc:mysql://localhost/testdb";
	static String driver = "com.mysql.jdbc.Driver";
	static String username = "tester";
	static String password = "tester";
	static boolean debug = false;
	
	public static void main(String[] args) throws Exception {
		Properties opt = new Properties();
		args = parseOpt(args, opt);
		
		if(args.length > 1 || opt.containsKey("help"))
			printExitHelp();
		
		if(opt.containsKey("driver"))
			driver = opt.getProperty("driver");
		if(opt.containsKey("url"))
			url = opt.getProperty("url");
		if(opt.containsKey("user"))
			username = opt.getProperty("user");
		if(opt.containsKey("pass"))
			password = opt.getProperty("pass");
		if(opt.containsKey("debug"))
			debug = true;
		
		dataSource = new MysqlDataSource();
		dataSource.setUrl(url);
		dataSource.setUser(username);
		dataSource.setPassword(password);
		
		if(debug){
			JDBCUtils.setLogWriter(System.out);	
		}
		
		testSimpleUpdate();
		testBindingVarsUpdate();
		testSimpleQuery();
		testBindingVarsQuery();
	}
	
	public static void printExitHelp(){
		System.out.println("USAGE: RunJDBCUtils [options]");
		System.out.println("[options]");
		System.out.println("  --help            Help page");
		System.out.println("  --driver=DRIVER   Default to com.mysql.jdbc.Driver");
		System.out.println("  --url=URL         Default to jdbc:mysql://localhost/testdb");
		System.out.println("  --user=USER       Default to tester");
		System.out.println("  --pass=PASS       Default to tester");
		System.out.println("NOTES:");
		System.out.println("  Mysql driver: com.mysql.jdbc.Driver");
		System.out.println("  Mysql url: jdbc:mysql://localhost/testdb");
		System.out.println("  Oracle driver: oracle.jdbc.OracleDriver");
		System.out.println("  Oracle url: jdbc:oracle:thin:tester/tester@myserver:1521:testdb");
		System.out.println("  Informix driver: com.informix.jdbc.IfxDriver");
		System.out.println("  Informix url: jdbc:informix-sqli://myserver:1525/testdb:INFORMIXSERVER=myserver");
		System.exit(1);
	}
	
	/** Parse and strip out both short and long options from args array */ 
	public static String[] parseOpt(String[] args, java.util.Properties opt){
		if(opt == null){
			opt = new java.util.Properties();
		}
		java.util.List argsList = new java.util.ArrayList();
		for(int i = 0, maxIndex = args.length; i < maxIndex; i++){
			String arg = args[i];
			if(arg.startsWith("--")){
				String[] s = arg.substring(2).split("=");
				if(s.length >=2){
					opt.setProperty(s[0], s[1]);
				}else{
					opt.setProperty(s[0], "true");
				}
			}else if(arg.startsWith("-")){
				String s = arg.substring(1);
				if(s.length() >1){
					opt.setProperty(s.substring(0, 1), s.substring(1));
				}else{
					opt.setProperty(s, "true");
				}
			}else{
				argsList.add(arg);
			}
		}
		
		return (String[])argsList.toArray(new String[0]);
	}

	public static void testSimpleUpdate() throws SQLException {
		System.out.println("testSimpleUpdate");
		
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = JDBCUtils.update(conn, 
				"INSERT INTO mytbl(FName,LName) VALUES('Tester', 'JDBCUtils')");
			System.out.println("[DEBUG] ret " + ret);
		} finally {
			JDBCUtils.close(conn);
		}
	}
	
	public static void testBindingVarsUpdate() throws SQLException {
		System.out.println("testBindingVarsUpdate");
		
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			int ret = JDBCUtils.update(conn, "INSERT INTO mytbl(FName,LName, Entry) VALUES(?,?, ?)", 
				new Object[]{ "Tester2", "JDBCUtils", new Timestamp(System.currentTimeMillis())});
			System.out.println("[DEBUG] ret " + ret);
		} finally {
			JDBCUtils.close(conn);
		}
	}
	
	public static void testSimpleQuery() throws SQLException {
		System.out.println("testSimpleQuery");
		
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			rs = JDBCUtils.query(conn, "SELECT * FROM mytbl");
			System.out.println(JDBCUtils.resultSetToString(rs));
		} finally {
			JDBCUtils.close(rs);
			JDBCUtils.close(conn);
		}
	}
	
	public static void testBindingVarsQuery() throws SQLException {
		System.out.println("testBindingVarsQuery");
		
		Connection conn = null;
		ResultSet rs = null;

		try {
			conn = dataSource.getConnection();
			rs = JDBCUtils.query(conn, "SELECT * FROM mytbl WHERE UserId > ? AND UserId < ?;",
				new Object[]{new Integer(2), new Integer(10)});
			System.out.println(JDBCUtils.resultSetToString(rs));
		} finally {
			JDBCUtils.close(rs);
			JDBCUtils.close(conn);
		}
	}
}
