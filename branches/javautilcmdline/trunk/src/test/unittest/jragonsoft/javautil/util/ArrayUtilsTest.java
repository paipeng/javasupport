package jragonsoft.javautil.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

/**
 *  Description of the Class
 *
 *@author     zemian
 *@version    $Id: ArrayUtilsTest.java 4 2006-03-16 15:27:19Z zemian $
 */
public class ArrayUtilsTest extends TestCase {
	protected void setUp() {
	}
	protected void tearDown() {
	}
	
	
	public void testAsList(){
		int[] a = {2,3,4};
		List l = ArrayUtils.asList(a);
		assertEquals(new Integer(2), l.get(0));
		assertEquals(new Integer(3), l.get(1));
		assertEquals(new Integer(4), l.get(2));
		
		String[] b = {"x", "WHY", "ZOO"};
		List sl = ArrayUtils.asList(b);
		assertEquals("x", sl.get(0));
		assertEquals("WHY", sl.get(1));
		assertEquals("ZOO", sl.get(2));
	}
	
	public void testToStringArray(){
		List a = new ArrayList();
		a.add("One");
		a.add("Three");
		
		String[] b = ArrayUtils.toStringArray(a);
		assertEquals("One", b[0]);
		assertEquals("Three", b[1]);
	}
	
	public void testToIntArray(){
		List a = new ArrayList();
		a.add(new Integer(1977));
		a.add(new Integer(2005));
		
		int[] b = ArrayUtils.toIntArray(a);
		assertEquals(1977,b[0]);
		assertEquals(2005,b[1]);
	}
	
	public void testSliceArray(){
		String[] test = { "A", "B", "C", "D", "E" };
		assertTrue(Arrays.equals(test, ArrayUtils.slice(test, 0, test.length -1, 1)));
		assertTrue(Arrays.equals(new String[]{"A", "B", "C"}, ArrayUtils.slice(test, 0, 2, 1)));
		assertTrue(Arrays.equals(new String[]{"C",  "D", "E"}, ArrayUtils.slice(test, 2, 4, 1)));	
		
		assertTrue(Arrays.equals(new String[]{"A", "C", "E"}, ArrayUtils.slice(test, 0, 4, 2)));
		assertTrue(Arrays.equals(new String[]{"A", "D"}, ArrayUtils.slice(test, 0, 4, 3)));
		assertTrue(Arrays.equals(new String[]{"B", "E"}, ArrayUtils.slice(test, 1, 4, 3)));
		
		
		assertTrue(Arrays.equals(new String[]{"A", "E"}, ArrayUtils.slice(test, 0, 4, 4)));
		assertTrue(Arrays.equals(new String[]{"C"}, ArrayUtils.slice(test, 2, 4, 4)));
		
		assertTrue(Arrays.equals(new String[]{"A"}, ArrayUtils.slice(test, 0, 4, 5)));
	}
	
	public void testSliceList(){
		List test = Arrays.asList(new String[] { "A", "B", "C", "D", "E" });
		assertEquals(test, ArrayUtils.slice(test, 0, test.size() -1, 1));
		assertEquals(Arrays.asList(new String[]{"A", "B", "C"}), ArrayUtils.slice(test, 0, 2, 1));
		assertEquals(Arrays.asList(new String[]{"C",  "D", "E"}), ArrayUtils.slice(test, 2, 4, 1));	
		
		assertEquals(Arrays.asList(new String[]{"A", "C", "E"}), ArrayUtils.slice(test, 0, 4, 2));
		assertEquals(Arrays.asList(new String[]{"A", "D"}), ArrayUtils.slice(test, 0, 4, 3));
		assertEquals(Arrays.asList(new String[]{"B", "E"}), ArrayUtils.slice(test, 1, 4, 3));
		
		
		assertEquals(Arrays.asList(new String[]{"A", "E"}), ArrayUtils.slice(test, 0, 4, 4));
		assertEquals(Arrays.asList(new String[]{"C"}), ArrayUtils.slice(test, 2, 4, 4));
		
		assertEquals(Arrays.asList(new String[]{"A"}), ArrayUtils.slice(test, 0, 4, 5));
	}
	
	public void testCopy(){		
		String[] test = { "A", "B", "C", "D", "E" };
		assertTrue(Arrays.equals(test, ArrayUtils.copy(test)));
		assertTrue(Arrays.equals(new String[]{"A", "B", "C"}, ArrayUtils.copy(test, 3)));
		
		assertTrue(Arrays.equals(new String[]{"A", "B", "C", "D"}, ArrayUtils.copy(test, 0, 3, 1)));
		assertTrue(Arrays.equals(new String[]{"E"}, ArrayUtils.copy(test, 4, 4, 1)));
	}
}

