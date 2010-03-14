/*
 *	  5/4/2006 Zemian Deng
 * 
 *	  Licensed under the Apache License, Version 2.0 (the "License");
 *	  you may not use this file except in compliance with the License.
 *	  You may obtain a copy of the License at
 * 
 *		  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *	  Unless required by applicable law or agreed to in writing, software
 *	  distributed under the License is distributed on an "AS IS" BASIS,
 *	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	  See the License for the specific language governing permissions and
 *	  limitations under the License.
 *																				 
 */

package jragonsoft.javautil.util;

import java.io.PrintStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Arrays;

/**
 * Provide basic JDBC utility and helper functions.
 * 
 * @author zemian
 * @version $Id: JDBCUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class JDBCUtils {
	/** Description of the Field */
	public final static String RECORD_SEP = System
			.getProperty("line.separator");

	/** Description of the Field */
	public final static String FIELD_SEP = "|";

	/** Writer for logging. */
	public static PrintStream logWriter = System.out; // set default to STDOUT.

	public static String logMsgPrefix = "JDBCUtils [DEBUG] ";

	public static void log(String msg) {
		if (logWriter != null) {
			logWriter.println(logMsgPrefix + msg);
		}
	}

	public static void setLogWriter(PrintStream out) {
		logWriter = out;
	}

	/** Set all binding parameters in array into stmt object */
	public static void setBindings(PreparedStatement stmt, Object bindings[])
			throws SQLException {
		StringBuffer sb = new StringBuffer();
		if (bindings != null && bindings.length > 0) {
			for (int aryIdx = 0, rsIdx = 1, max = bindings.length; aryIdx < max; aryIdx++, rsIdx++) {
				if (bindings[aryIdx] == null) {
					stmt.setNull(rsIdx, Types.NULL);
				} else {
					stmt.setObject(rsIdx, bindings[aryIdx]);
				}
			}
		}
	}

	/**
	 * Simple query. Stmt and RS will remain open for returned ref.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param sql
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static ResultSet query(Connection conn, String sql)
			throws SQLException {
		Statement stmt = null;
		ResultSet rs = null;

		log("QUERY: " + sql);
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);
		rs = stmt.executeQuery(sql);

		return rs;
	}

	/**
	 * Variable binding query. Stmt and RS will remain open for returned ref.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param sql
	 *            Description of the Parameter
	 * @param bindings
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static ResultSet query(Connection conn, String sql,
			Object bindings[]) throws SQLException {
		PreparedStatement stmt = null;
		ResultSet rs = null;

		log("QUERY: " + sql);
		stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_READ_ONLY);

		setBindings(stmt, bindings);
		log("BINDING VARS: " + Arrays.asList(bindings).toString());
		rs = stmt.executeQuery();

		return rs;
	}

	/**
	 * Simple updatable query execution.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param sql
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static int update(Connection conn, String sql) throws SQLException {
		Statement stmt = null;

		log("QUERY: " + sql);
		stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		int ret = stmt.executeUpdate(sql);
		close(stmt);

		return ret;
	}

	/**
	 * Variables binding updatable query execution.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param sql
	 *            Description of the Parameter
	 * @param bindings
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static int update(Connection conn, String sql, Object bindings[])
			throws SQLException {
		PreparedStatement stmt = null;

		log("QUERY: " + sql);
		stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
				ResultSet.CONCUR_UPDATABLE);

		setBindings(stmt, bindings);
		log("BINDING VARS: " + Arrays.asList(bindings).toString());
		int ret = stmt.executeUpdate();
		stmt.close();

		return ret;
	}

	/**
	 * Convert ResultSet to a String in table view. RS will be close afterward.
	 * 
	 * @param rs
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception SQLException
	 *                Description of the Exception
	 */
	public static String resultSetToString(ResultSet rs, String contentPrefix,
			String contentSuffix, String recordPrefix, String recordSuffix,
			String fieldPrefix, String fieldSuffix) throws SQLException {

		StringBuffer sb = new StringBuffer();
		sb.append(contentPrefix);

		ResultSetMetaData rsmd = rs.getMetaData();
		int numcols = rsmd.getColumnCount();
		while (rs.next()) {
			sb.append(recordPrefix);
			for (int i = 1; i <= numcols; i++) {
				sb.append(fieldPrefix).append(
						StringUtils.toString(rs.getObject(i))).append(
						fieldSuffix);
			}
			sb.append(recordSuffix);
		}
		//close(rs); // Do not close for further use. 042506

		sb.append(contentSuffix);
		return sb.toString();
	}

	public static String resultSetToString(ResultSet rs) throws SQLException {
		return resultSetToString(rs, "", "", "", RECORD_SEP, "", FIELD_SEP);
	}

	public static String resultSetToXML(ResultSet rs) throws SQLException {
		return resultSetToString(rs, "\n<resultset>", "\n</resultset>",
				"\n  <row>", "\n  </row>", "\n    <field>", "</field>");
	}

	/** Exctract and return all the column names in the ResultSet instacne. */
	public static String[] getResultSetColumnNames(ResultSet rs) {
		try {
			ResultSetMetaData rsmd = rs.getMetaData();
			int max = rsmd.getColumnCount();
			String[] result = new String[max];
			for (int aryIdx = 0, rsIdx = 1; aryIdx < max; aryIdx++, rsIdx++) {
				result[aryIdx] = rsmd.getColumnName(rsIdx);
			}
			return result;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close out connection, statement, and result set object safely.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param stmt
	 *            Description of the Parameter
	 * @param rs
	 *            Description of the Parameter
	 */
	public static void close(Connection conn, Statement stmt, ResultSet rs) {
		close(conn);
		close(stmt);
		close(rs);
	}

	/**
	 * Close out connection, and result set object safely.
	 * 
	 * @param conn
	 *            Description of the Parameter
	 * @param rs
	 *            Description of the Parameter
	 */
	public static void close(Connection conn, ResultSet rs) {
		close(conn);
		close(rs);
	}

	/**
	 * Close out connection object safely.
	 * 
	 * @param connection
	 *            Description of the Parameter
	 */
	public static void close(Connection connection) {
		try {
			if (connection != null) {
				connection.close();
				connection = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close out statement object safely.
	 * 
	 * @param stmt
	 *            Description of the Parameter
	 */
	public static void close(Statement stmt) {
		try {
			if (stmt != null) {
				stmt.close();
				stmt = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Close out result set object safely.
	 * 
	 * @param rs
	 *            Description of the Parameter
	 */
	public static void close(ResultSet rs) {
		try {
			if (rs != null) {
				rs.close();
				rs = null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
