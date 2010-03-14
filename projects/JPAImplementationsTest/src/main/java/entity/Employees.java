package entity;

import java.io.Serializable;
import java.sql.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="employees") 
@NamedQueries( {

		// query returning number of female employees
		@NamedQuery(name = "findAllWomen", query = "select count(t) from Employees as t where t.gender = 'F'"),

//		// query returning number of female employees earning more than a certain salary
//		@NamedQuery(name = "findAllWomenMaxSalary", query = "select count(t) from Employees as t where t.gender='F' and :salary < " + 
//				"(select max(s.salary) from Salaries s where s.empNo=t.empNo)"),
		
		// query returning number of female employees earning more than a certain salary
		@NamedQuery(name = "findAllWomenMaxSalary", query = "select COUNT(DISTINCT s.empNo) from Salaries s, Employees e " + 
				"where s.empNo.empNo = e.empNo and s.salary > :salary and e.gender='F'"),

		// query returning number of male employees
		@NamedQuery(name = "findAllMen", query = "select count(t) from Employees as t where t.gender = 'M'"),

		// query returning employees hired since a fixed date
		@NamedQuery(name = "findAllHiredAfterDate", query = "select count(t) from Employees as t where t.hireDate > :date"),

		// query returning employees born later than a fixed date
		@NamedQuery(name = "findAllBirthAfterDate", query = "select count(t) from Employees as t where t.birthDate > :date"), })

public class Employees implements Serializable {
	public Employees (Employees emp) {
		this.birthDate = emp.getBirthDate();
		this.firstName = emp.getFirstName();
		this.lastName = emp.getLastName();
		this.gender = emp.getGender();
		this.hireDate = emp.getHireDate();
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY) 
	@Column(name="emp_no")
	private int empNo;

	@Column(name="birth_date")
	private Date birthDate;

	@Column(name="first_name")
	private String firstName;

	@Column(name="last_name")
	private String lastName;

	@Column(name="gender")
	private String gender;

	@Column(name="hire_date")
	private Date hireDate;

	@OneToMany(mappedBy="empNo")
	private Set<DeptEmp> deptEmpCollection;

	@OneToMany(mappedBy="empNo")
	private Set<DeptManager> deptManagerCollection;

	@OneToMany(mappedBy="empNo")
	private Set<Salaries> salariesCollection;

	@OneToMany(mappedBy="empNo")
	private Set<Titles> titlesCollection;

	private static final long serialVersionUID = 1L;

	public Employees() {
		super();
	}

	public int getEmpNo() {
		return this.empNo;
	}

	public void setEmpNo(int empNo) {
		this.empNo = empNo;
	}

	public Date getBirthDate() {
		return this.birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getGender() {
		return this.gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Date getHireDate() {
		return this.hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Set<DeptEmp> getDeptEmpCollection() {
		return this.deptEmpCollection;
	}

	public void setDeptEmpCollection(Set<DeptEmp> deptEmpCollection) {
		this.deptEmpCollection = deptEmpCollection;
	}

	public Set<DeptManager> getDeptManagerCollection() {
		return this.deptManagerCollection;
	}

	public void setDeptManagerCollection(Set<DeptManager> deptManagerCollection) {
		this.deptManagerCollection = deptManagerCollection;
	}

	public Set<Salaries> getSalariesCollection() {
		return this.salariesCollection;
	}

	public void setSalariesCollection(Set<Salaries> salariesCollection) {
		this.salariesCollection = salariesCollection;
	}

	public Set<Titles> getTitlesCollection() {
		return this.titlesCollection;
	}

	public void setTitlesCollection(Set<Titles> titlesCollection) {
		this.titlesCollection = titlesCollection;
	}

}
