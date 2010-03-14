package deng.javautil.util;

import java.util.*;
import junit.framework.*;

public class RegexUtilsTest extends TestCase{
	
	protected void setUp(){
	}	
	protected void tearDown(){
	}
	
	public void testEscapeRegex(){
		assertEquals("\\+", RegexUtils.escapeRegex("+"));
		assertEquals("\\*", RegexUtils.escapeRegex("*"));
		assertEquals("\\?", RegexUtils.escapeRegex("?"));
		assertEquals("\\[", RegexUtils.escapeRegex("["));
		assertEquals("\\]", RegexUtils.escapeRegex("]"));
		assertEquals("\\(", RegexUtils.escapeRegex("("));
		assertEquals("\\)", RegexUtils.escapeRegex(")"));
		assertEquals("\\$", RegexUtils.escapeRegex("$"));
		assertEquals("\\^", RegexUtils.escapeRegex("^"));
		assertEquals("\\.", RegexUtils.escapeRegex("."));
		assertEquals("\\\\", RegexUtils.escapeRegex("\\"));
		
		assertEquals("\\\\\\.\\^\\$\\)\\(\\]\\[\\?\\*\\+", RegexUtils.escapeRegex("\\.^$)(][?*+"));
		assertEquals("ABC\\\\\\.\\^\\$\\)\\(\\]\\[\\?\\*\\+", RegexUtils.escapeRegex("ABC\\.^$)(][?*+"));
		assertEquals("\\\\\\.\\^\\$\\)\\(\\]\\[\\?\\*\\+DEF", RegexUtils.escapeRegex("\\.^$)(][?*+DEF"));
		assertEquals("\\\\\\.\\^\\$\\)\\(\\]XYZ\\[\\?\\*\\+", RegexUtils.escapeRegex("\\.^$)(]XYZ[?*+"));
		assertEquals("ABC\\\\\\.\\^\\$\\)\\(\\]\\[\\?\\*\\+DEF", RegexUtils.escapeRegex("ABC\\.^$)(][?*+DEF"));
	}
	
	public void testExpandSubstitutions(){
		Properties map = new Properties();
		map.setProperty("name", "Zemian");
		map.setProperty("dob", "09/17/77");
		map.setProperty("skill", "Java");	
		map.setProperty("test.dot", "test dot");		
		
		String test ="My name is ${name}";
		assertEquals("My name is Zemian", RegexUtils.expandSubstitutions(test, map));
		
		test ="${skill}";
		assertEquals("Java", RegexUtils.expandSubstitutions(test, map));
		
		//test ="${test.dot} should work";
		//assertEquals("test dot should work", RegexUtils.expandSubstitutions(test, map));
		
		//test ="${name} was born in ${dob} and good at ${skill}";
		//assertEquals("Zemian was born in 09/17/77 and good at Java", RegexUtils.expandSubstitutions(test, map));
		
		test ="$${test}";
		assertEquals("${test}", RegexUtils.expandSubstitutions(test, map));
	}
}
