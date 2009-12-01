package deng.javaexamples;

import java.util.Arrays;

public class StringDemo {
	public static void main(String[] args) {
		System.out.println(Arrays.asList("foo".split(" ")));
		System.out.println(Arrays.asList("5".split("-|:")));
		System.out.println(Arrays.asList("5-10".split("-|:")));
		System.out.println(Arrays.asList("5-10:2".split("-|:")));
	}
}
