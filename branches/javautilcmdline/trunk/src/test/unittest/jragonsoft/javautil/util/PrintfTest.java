package jragonsoft.javautil.util;

import jragonsoft.javautil.support.Printf;
import junit.framework.TestCase;

public class PrintfTest extends TestCase{
		
	protected void setUp(){
	}	
	protected void tearDown(){
	}

	public void testPrintf(){
		assertEquals("This is a test case for Printf\n", Printf.format("This is a test case for Printf\n"));
		assertEquals("My name is Zemian\n", Printf.format("%s\n", "My name is Zemian"));
		assertEquals("2005\n", Printf.format("%d\n", 2005));
		assertEquals("2005.2005\n", Printf.format("%f\n", 2005.2005));
		
		assertEquals("Zemian was born in 1977", Printf.format("%s was born in %d", Printf.add("Zemian").add(1977)));
		
		assertEquals("test with %", Printf.format("%s with %%", Printf.add("test")));		
		assertEquals("You should see 1 percent sign here. %", Printf.format("You should see %d percent sign here. %%", Printf.add(1)));
		
		
		//assertEquals("% beginning 0.999", Printf.format("%% begining %f", Printf.add(0.999)));
		assertEquals("test1        101   1.2234", Printf.format("%-10s %5d %8.4f", Printf.add("test1").add(101).add(1.223444)));
		assertEquals("test1        101 1.2234  ", Printf.format("%-10s %+5d %-8.4f", Printf.add("test1").add(101).add(1.223444)));
		assertEquals("test1      101   1.2234  ", Printf.format("%-10s %-5d %-8.4f", Printf.add("test1").add(101).add(1.223444)));
		assertEquals("test1      00101 1.2234  ", Printf.format("%-10s %05d %-8.4f", Printf.add("test1").add(101).add(1.223444)));
		              
		assertEquals("A test with $ value My name is Zemian", Printf.format("A test with $ value %s", Printf.add("My name is Zemian")));		
		assertEquals("A test with $ value My name is $Zemian", Printf.format("A test with $ value %s", Printf.add("My name is $Zemian")));
		
		assertEquals("\\", Printf.format("%s", Printf.add("\\")));
		assertEquals("$", Printf.format("%s", Printf.add("$")));
		assertEquals("(", Printf.format("%s", Printf.add("(")));
		assertEquals(")", Printf.format("%s", Printf.add(")")));
		assertEquals("[", Printf.format("%s", Printf.add("[")));
		assertEquals("]", Printf.format("%s", Printf.add("]")));
		assertEquals("+", Printf.format("%s", Printf.add("+")));
		assertEquals("-", Printf.format("%s", Printf.add("-")));
		assertEquals(".", Printf.format("%s", Printf.add(".")));
		assertEquals("*", Printf.format("%s", Printf.add("*")));
		assertEquals("?", Printf.format("%s", Printf.add("?")));
	}
}
