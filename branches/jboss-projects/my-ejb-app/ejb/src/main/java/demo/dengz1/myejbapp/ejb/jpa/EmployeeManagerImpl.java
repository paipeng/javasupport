package demo.dengz1.myejbapp.ejb.jpa;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

@Remote(EmployeeManager.class)
@Stateful(name="EmployeeManagerImpl")
public class EmployeeManagerImpl implements EmployeeManager {
	public static Logger logger = Logger.getLogger(EmployeeManagerImpl.class);
	
	@PersistenceContext(unitName = "DEMO_PU")
    private EntityManager entityManager;

    public int addEmployee(Employee employee) throws Exception {
    	logger.info("Adding new employee " + employee.getLastName());
        entityManager.persist(employee);
    	logger.info("Employee added successfully. Id=" + employee.getId());
    	
    	return employee.getId();
    }

    public void deleteEmployee(Employee employee) throws Exception {
        entityManager.remove(employee);
    }

    public List<Employee> getEmployees() throws Exception {
        Query query = entityManager.createQuery("SELECT e from Employee as e");
        return query.getResultList();
    }

}
