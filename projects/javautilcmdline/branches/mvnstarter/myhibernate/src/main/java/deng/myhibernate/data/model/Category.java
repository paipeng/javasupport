package deng.myhibernate.data.model;

import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "category")
public class Category extends Model {
	@Id
	@GeneratedValue
	@Column(name = "id")
	private Short id;

	@Column(name = "name")
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "category")
	private Set<Item> items = new TreeSet<Item>();

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (this.getName() == null || !(obj instanceof Category)) {
			return false;
		}
		Category model = (Category) obj;
		return model.getName().equals(this.getName());
	}

	@Override
	public int hashCode() {
		if (this.getName() == null) {
			return super.hashCode();
		}
		return this.getName().hashCode();
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Set<Item> getItems() {
		return items;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
