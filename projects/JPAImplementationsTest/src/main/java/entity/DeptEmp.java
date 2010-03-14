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
@Table(name="dept_emp")
public class DeptEmp implements Serializable {
	@EmbeddedId
	private DeptEmpPK pk;

	@Column(name="from_date")
	private Date fromDate;

	@Column(name="to_date")
	private Date toDate;

	@ManyToOne
	@JoinColumn(name="emp_no", insertable=false, updatable=false)
	private Employees empNo;

	@ManyToOne
	@JoinColumn(name="dept_no", insertable=false, updatable=false)
	private Departments deptNo;

	private static final long serialVersionUID = 1L;

	public DeptEmp() {
		super();
	}

	public DeptEmpPK getPk() {
		return this.pk;
	}

	public void setPk(DeptEmpPK pk) {
		this.pk = pk;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
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

	public Departments getDeptNo() {
		return this.deptNo;
	}

	public void setDeptNo(Departments deptNo) {
		this.deptNo = deptNo;
	}

}
