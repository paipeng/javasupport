package demo.dengz1.myejbapp.ejb.jpa;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="EMPLOYEES")
public class Employee implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="EMPLOYEE_ID")
	@Id()
    @GeneratedValue(generator="EMPLOYEES_SEQ")
    @SequenceGenerator(name="EMPLOYEES_SEQ", sequenceName="EMPLOYEES_SEQ")
    private int id;	

	@Column(name="LAST_NAME")
	private String lastName;

	@Column(name="FIRST_NAME")
	private String firstName;

	@Column(name="EMAIL")
	private String email;

	@Column(name="PHONE_NUMBER")
	private String phoneNumber;

	@Column(name="HIRE_DATE")
	private Date hireDate;

	@Column(name="SALARY")
	private double salary;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public double getSalary() {
		return salary;
	}

	public void setSalary(double salary) {
		this.salary = salary;
	}	
}
