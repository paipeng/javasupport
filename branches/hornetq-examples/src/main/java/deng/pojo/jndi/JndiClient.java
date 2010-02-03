package deng.pojo.jndi;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class JndiClient {
	
	public static void main(String[] args) throws Exception {
		JndiClient bean = new JndiClient();
		String name = args[0];
		bean.lookup(name);
	}
	
	public void lookup(String name) throws Exception {
		Context ctx = new InitialContext();
		try {
			Object obj = ctx.lookup(name);
			String objStr = ToStringBuilder.reflectionToString(obj, ToStringStyle.MULTI_LINE_STYLE);
			System.out.println(objStr);
		} finally {
			if (ctx != null) {
				ctx.close();
			}
		}
	}
}
