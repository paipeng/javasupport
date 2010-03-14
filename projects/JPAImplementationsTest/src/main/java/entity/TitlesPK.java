package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class TitlesPK implements Serializable {
	@Column(name="emp_no")
	private int empNo2;

	private String title;

	@Column(name="from_date")
	private Date fromDate;

	private static final long serialVersionUID = 1L;

	public TitlesPK() {
		super();
	}

	public int getEmpNo2() {
		return this.empNo2;
	}

	public void setEmpNo2(int empNo2) {
		this.empNo2 = empNo2;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getFromDate() {
		return this.fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if ( ! (o instanceof TitlesPK)) {
			return false;
		}
		TitlesPK other = (TitlesPK) o;
		return (this.empNo2 == other.empNo2)
			&& this.title.equals(other.title)
			&& this.fromDate.equals(other.fromDate);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.empNo2;
		hash = hash * prime + this.title.hashCode();
		hash = hash * prime + this.fromDate.hashCode();
		return hash;
	}

}
