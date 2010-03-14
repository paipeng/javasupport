package deng.myhibernate.data.model;

import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class Model {

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
