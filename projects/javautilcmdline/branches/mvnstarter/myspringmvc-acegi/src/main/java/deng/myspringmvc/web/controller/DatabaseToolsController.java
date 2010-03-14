package deng.myspringmvc.web.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

/**
 * The default MethodNameResolver is InternalPathMethodNameResolver.
 * 
 * @author zemian
 *
 */
public class DatabaseToolsController extends MultiActionController {
	public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("dbtools/index");
		return mv;
	}
	
	/**
	 * The default path of H2Database is at $HOME/<dbname>
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView connTestH2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("dbtools/connTestH2");

		String driver = "org.h2.Driver";
		String url = "jdbc:h2:~/test";
		String user = "sa";
		String pass ="";
		
		mv.addObject("driver", driver);
		mv.addObject("url", url);
		mv.addObject("user", user);
		
		Class.forName(driver);
		Connection conn = DriverManager.getConnection(url, user, pass);
		
		Statement stm = null;
		ResultSet rs = null;
		String createTestTable = "CREATE TABLE IF NOT EXISTS test_hello_world(id INT, name VARCHAR)";
		String insertTestTable = "INSERT INTO test_hello_world VALUES(1, 'Hello'), (2, 'World')";
		String deleteTestTable = "DELETE FROM test_hello_world";
		String queryTestTable = "SELECT * FROM test_hello_world";
		String queryDatabseInfo = "SELECT DATABASE_PATH() AS DATABASE_PATH";
		
		int updateResult = 0;
		ResultSetMetaData rsmd = null;
		try{
			stm = conn.createStatement();
			updateResult = stm.executeUpdate(createTestTable);
			mv.addObject("createTestTableResult", updateResult);

			updateResult = stm.executeUpdate(insertTestTable);
			mv.addObject("insertTestTableResult", updateResult);
						
			rs = stm.executeQuery(queryTestTable);
			List<Object> queryTestTableResult = new ArrayList<Object>();
			rsmd = rs.getMetaData();
			while(rs.next()){
				Map<String, Object> row = new HashMap<String, Object>();
				for(int i = 1; i<=rsmd.getColumnCount();i++){
					row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
				queryTestTableResult.add(row);
			}
			mv.addObject("queryTestTableResult", queryTestTableResult);
			
			updateResult = stm.executeUpdate(deleteTestTable);
			mv.addObject("deleteTestTableResult", updateResult);
			
			rs = stm.executeQuery(queryDatabseInfo);
			List<Object> queryDatabseInfoResult = new ArrayList<Object>();
			rsmd = rs.getMetaData();
			while(rs.next()){
				Map<String, Object> row = new HashMap<String, Object>();
				for(int i = 1; i<=rsmd.getColumnCount();i++){
					row.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
				}
				queryDatabseInfoResult.add(row);
			}
			mv.addObject("queryDatabseInfoResult", queryDatabseInfoResult);
		}finally{
			if(rs != null) rs.close();
			if(stm != null) stm.close();
		}
		
		return mv;
	}
}
