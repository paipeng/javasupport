package entity;

import java.io.Serializable;
import java.sql.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Titles implements Serializable {
	@EmbeddedId
	private TitlesPK pk;

	@Column(name="to_date")
	private Date toDate;

	@ManyToOne
	@JoinColumn(name="emp_no", insertable=false, updatable=false)
	private Employees empNo;

	private static final long serialVersionUID = 1L;

	public Titles() {
		super();
	}

	public TitlesPK getPk() {
		return this.pk;
	}

	public void setPk(TitlesPK pk) {
		this.pk = pk;
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
