package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="salaries")
public class Salaries implements Serializable {
	@EmbeddedId
	private SalariesPK pk;

	private int salary;

	@Column(name="to_date")
	private Date toDate;

	@ManyToOne
	@JoinColumn(name="emp_no", insertable=false, updatable=false)
	private Employees empNo;

	private static final long serialVersionUID = 1L;

	public Salaries() {
		super();
	}

	public SalariesPK getPk() {
		return this.pk;
	}

	public void setPk(SalariesPK pk) {
		this.pk = pk;
	}

	public int getSalary() {
		return this.salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public Date getToDate() {
		return this.toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Employees getEmpNo() {
		return this.empNo;
	}

	public void setEmpNo(Employees empNo) {
		this.empNo = empNo;
	}

}
