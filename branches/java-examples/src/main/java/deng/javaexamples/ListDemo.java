package deng.javaexamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListDemo {
	public static void main(String[] args) {
		List<MyBean> ls = new ArrayList<MyBean>();
		ls.add(new MyBean("b1"));
		ls.add(new MyBean("b2"));
		
		System.out.println(ls.toString());
		
		List<MyBean> ls2 = Arrays.asList(
				new MyBean[]{
						new MyBean("b1"), new MyBean("b2")		
				});
		System.out.println(ls2.toString());
	}
}
