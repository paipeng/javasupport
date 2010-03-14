/**
 * Created by Zemian Deng 08062009
 */
package ztool;

import static ztool.CliHelper.getInputStream;
import static ztool.CliHelper.readText;

import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Sql extends CliBase {
	public static String DEFULT_TRANSPOSE_FMT = "%25s: %s";
	private String driverClass;
	private String url;
	private String username;
	private String password;
	private String lineFormat;
	private boolean showHeader = false;
	private String view;
	private File sqlFile;
	

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help",                     "Display this help page.").
		setOption("debug",                    "Show extra debug info.").
		setOption("driverClass=<java_class>", "Jdbc driver class implementation name.").
		setOption("url=<jdbc_url>",           "Jdbc driver class implementation name.").
		setOption("username=<text>",          "Database username.").
		setOption("password=<text>",          "Database password.").
		setOption("sqlFile=<file>",           "Read file for SQL statements.").
		setOption("lineFormat=<printf_fmt>",  "Specify string format to use when print row.").
		setOption("view=<table|transpose>",   "Select view type to print result.").
		setOption("showHeader",               "Do not display column name header for table view.").
		setSummary(
			"Execute sql statement(s) or from input script. If input script and\n" +
			"sql_statement arguments are omitted, it will read from STDIN. Result will\n" +
			"display in transpose format as default.\n" +
			"").
		setUsage("ztool Sql [Options] [sql_statement sql_statement ...]").
		setExamples(
			"  ztool Sql --driverClass=com.mysql.jdbc.Driver " +
			"--url=jdbc:mysql://localhost:3306/mysql " +
			"\"SELECT USER, HOST FROM USER\"\n" +
			"  ztool Sql --driverClass=oracle.jdbc.driver.OracleDriver " +
			"--url=jdbc:oracle:thin:@localhost:1521:XE " +
			"--username=scott " +
			"--password=tiger " +
			"\"SELECT SYSDATE FROM DUAL\"\n" +
			"");
	}

	@Override
	public void run(Options opts) {
		driverClass = opts.get("driverClass", "oracle.jdbc.driver.OracleDriver");
		url = opts.get("url", "jdbc:oracle:thin:@localhost:1521:XE");
		username = opts.get("username", "scott");
		password = opts.get("password", "tiger");
		lineFormat = opts.get("lineFormat", lineFormat);
		view = opts.get("view", "transpose");
		showHeader = opts.has("showHeader");
		debug = opts.has("debug");
		sqlFile = opts.getFile("sqlFile", null);
		
		List<String> args = opts.getArgs();
		try {
			if (debug) {
				System.out.println("Loading JDBC driver: " + driverClass);
			}
			Class.forName(driverClass); //Load driver class

			if (debug) {
				System.out.println("Connecting to: " + url);
				System.out.println("With username: " + username);
			}
			Connection conn = DriverManager.getConnection(url, username, password);
			
			if (debug) {
				System.out.println("Database connection established!");
			}	
			try {
				StringBuilder sql = null;
				if (args.size() >= 1) {
					sql = new StringBuilder();
					for (int i = 0; i < args.size(); i++) {
						sql.append(args.get(i));
						
						if (i + 1 < args.size() && !args.get(i).trim().endsWith(";")) {
							//AUTO Append ; separator for each statement.
							sql.append(";\n");
						}
					}
				} else {
					InputStream ins = getInputStream(sqlFile, System.in);
					sql = new StringBuilder(readText(ins));
				}
				runSql(conn, sql.toString());
			} finally {
				if (debug) {
					System.out.println("Closing out connection object.");
				}
				if(conn != null) { conn.close(); }
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void runSql(Connection conn, String sql) throws Exception {
		Statement stmt = conn.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		ResultSet rs = stmt.executeQuery(sql);
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			if (view.equals("table")) {
				String tableViewFmt = getTableViewFmt(rs);				
				if (debug) {
					System.out.println("TableViewFmt " + tableViewFmt);
				}
				
				if (showHeader) {
					printTableHeader(tableViewFmt, rs);
				}

				int colCount = rsmd.getColumnCount();
				while (rs.next()) {
					printTableRow(tableViewFmt, colCount, rs);
				}
			} else if (view.equals("transpose")) {
				int colCount = rsmd.getColumnCount();
				String fmt = (lineFormat == null) ? DEFULT_TRANSPOSE_FMT : lineFormat;
				if (debug) {
					System.out.println("TransposeViewFmt " + fmt);
				}		
				
				while (rs.next()) {
					printTransposeRow(fmt, colCount, rs);
				}
			} else {
				throw new RuntimeException("Invalid view " + view);
			}
		} finally {
			if (debug) {
				System.out.println("Closing out result set and statement objects.");
			}
			if(rs != null) { rs.close(); }
			if(stmt != null) { stmt.close(); }
		}
	}
	
	private String getTableViewFmt(ResultSet rs) throws SQLException {
		if (lineFormat == null) {		
			ResultSetMetaData rsmd = rs.getMetaData();
			int colCount = rsmd.getColumnCount();
			
			StringBuilder fmtSB = new StringBuilder();
			for(int i = 1; i <= colCount; i++) {
				fmtSB.append("%s");
				if(i + 1 <= colCount) {
					fmtSB.append('\t');
				}
			}
			return fmtSB.toString();
		} else {
			return lineFormat;
		}
	}

	private void printTableHeader(String fmt, ResultSet rs) throws SQLException {
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		
		Object[] params = new Object[colCount];
		for(int i = 1; i <= colCount; i++) {
			params[i - 1] = rsmd.getColumnName(i);
		}
		System.out.printf(fmt, params);		
		System.out.println();
	}

	private void printTableRow(String fmt, int colCount, ResultSet rs) throws Exception {
		Object[] params = new Object[colCount];
		do {
			for(int i = 1; i <= colCount; i++) {
				params[i - 1] = rs.getObject(i);
			}
			System.out.printf(fmt, params);
			System.out.println();
		} while (rs.next());
	}

	private void printTransposeRow(String fmt, int colCount, ResultSet rs) throws Exception {
		ResultSetMetaData rsmd = rs.getMetaData();
		
		for(int i = 1; i <= colCount; i++) {
			System.out.printf(fmt, rsmd.getColumnName(i), rs.getObject(i));
			System.out.println();
		}
		System.out.println();
	}
}
