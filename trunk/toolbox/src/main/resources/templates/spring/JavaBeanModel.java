package ${packageName};

import java.util.*;
import javax.persistence.*;

import org.apache.commons.lang.builder.ToStringBuilder; 

@Entity @Table(name = "${beanName}")
public class ${className} {
	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}	
	<% for (field in fields) {%>
	${field[3]}
	private ${field[1]} ${field[0]};
	public void set${field[2]}(${field[1]} that){ this.${field[0]} = that; }
	public ${field[1]} get${field[2]}(){ return this.${field[0]}; }
	<% } %>
}
