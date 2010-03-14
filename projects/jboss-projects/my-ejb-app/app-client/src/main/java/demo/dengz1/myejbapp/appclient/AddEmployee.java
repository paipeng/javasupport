package demo.dengz1.myejbapp.appclient;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import demo.dengz1.myejbapp.ejb.jpa.Employee;
import demo.dengz1.myejbapp.ejb.jpa.EmployeeManager;

public class AddEmployee {
	public static void main(String[] args) {
		Context ctx = null;

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jnp.interfaces.NamingContextFactory");
			props.setProperty(Context.PROVIDER_URL, "jnp://localhost:1099");

			// Let user override the parameters if exists.
			props.putAll(System.getProperties());
			
			ctx = new InitialContext(props);
			System.out.println("Looking up EmployeeManager");
			EmployeeManager employeeManager = (EmployeeManager) ctx.lookup("EmployeeManagerImpl/remote");
			
			Employee employee = new Employee();
			employee.setLastName("Tester" + System.currentTimeMillis());
			
			int newId = employeeManager.addEmployee(employee);
			employee.setId(newId);  //TODO: how to make this automated?
			
			System.out.println("Added employee: " + employee.getLastName() + " with id " + employee.getId());
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
