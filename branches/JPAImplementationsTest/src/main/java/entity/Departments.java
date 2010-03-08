package entity;

import java.io.Serializable;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Departments implements Serializable {
	@Id
	@Column(name="dept_no")
	private String deptNo;

	@Column(name="dept_name")
	private String deptName;

	@OneToMany(mappedBy="deptNo")
	private Set<DeptEmp> deptEmpCollection;

	@OneToMany(mappedBy="deptNo")
	private Set<DeptManager> deptManagerCollection;

	private static final long serialVersionUID = 1L;

	public Departments() {
		super();
	}

	public String getDeptNo() {
		return this.deptNo;
	}

	public void setDeptNo(String deptNo) {
		this.deptNo = deptNo;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
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

}
