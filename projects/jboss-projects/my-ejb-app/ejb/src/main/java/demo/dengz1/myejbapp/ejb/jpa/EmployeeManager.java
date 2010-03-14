package demo.dengz1.myejbapp.ejb.jpa;

import java.util.List;


public interface EmployeeManager {
	int addEmployee(Employee employee) throws Exception ;

    void deleteEmployee(Employee employee) throws Exception ;

    List<Employee> getEmployees() throws Exception ;
}
