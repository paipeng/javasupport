package demo.dengz1.myejbapp.appclient;

import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import demo.dengz1.myejbapp.ejb.jpa.Employee;
import demo.dengz1.myejbapp.ejb.jpa.EmployeeManager;

public class GetEmployees {
	public static Logger logger = Logger.getLogger(GetEmployees.class);
	public static void main(String[] args) {
		Context ctx = null;

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.setProperty(Context.PROVIDER_URL, "jnp://localhost:1099");

			// Let user override the parameters if exists.
			props.putAll(System.getProperties());
			
			ctx = new InitialContext(props);
			logger.debug("Looking up EmployeeManager.");
			EmployeeManager employeeManager = (EmployeeManager) ctx.lookup("EmployeeManagerImpl/remote");
			logger.info("Found employeeManager " + employeeManager);

			logger.debug("Getting emmployee list");
			List<Employee> employees = employeeManager.getEmployees();
			logger.info("There is " + employees.size() + " movies found.");
			for (Employee m : employees) {
				logger.info("Employee: " + m.getId() + " - " + 
						m.getFirstName() + " " + m.getLastName());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {			
			if (ctx != null) {
				try {
					ctx.close();
				} catch (NamingException e) {
					throw new RuntimeException(e);
				}
			}
		}
	}
}
