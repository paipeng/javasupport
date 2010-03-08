package entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class DeptEmpPK implements Serializable {
	@Column(name="emp_no")
	private int empNo2;

	@Column(name="dept_no")
	private String deptNo2;

	private static final long serialVersionUID = 1L;

	public DeptEmpPK() {
		super();
	}

	public int getEmpNo2() {
		return this.empNo2;
	}

	public void setEmpNo2(int empNo2) {
		this.empNo2 = empNo2;
	}

	public String getDeptNo2() {
		return this.deptNo2;
	}

	public void setDeptNo2(String deptNo2) {
		this.deptNo2 = deptNo2;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof DeptEmpPK)) {
			return false;
		}
		DeptEmpPK other = (DeptEmpPK) o;
		return (this.empNo2 == other.empNo2)
			&& this.deptNo2.equals(other.deptNo2);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.empNo2;
		hash = hash * prime + this.deptNo2.hashCode();
		return hash;
	}

}
