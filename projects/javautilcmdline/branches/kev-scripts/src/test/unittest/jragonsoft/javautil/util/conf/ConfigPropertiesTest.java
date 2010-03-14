package jragonsoft.javautil.util.conf;

import java.util.Set;

import jragonsoft.javautil.conf.ConfigProperties;
import junit.framework.TestCase;

public class ConfigPropertiesTest extends TestCase {
	protected void setUp() throws Exception {
		super.setUp();		
	}

	public void testNameExpansion(){
		ConfigProperties testProp = new ConfigProperties();
		testProp.setProperty("name", "Zemian Deng");
		testProp.setProperty("greeting", "Hello, ${name}.");
		assertEquals(testProp.getProperty("greeting"), "Hello, Zemian Deng.");
	}
	
	public void findPropertyNames(){
		ConfigProperties testProp = new ConfigProperties();
		testProp.setProperty("name", "Zemian Deng");
		testProp.setProperty("greeting", "Hello, ${name}.");
		testProp.setProperty("naming", "First Last");
		testProp.setProperty("greetingType", "First Last");
		
		Set namSet = testProp.findPropertyNames("nam");
		assertTrue(namSet.size()== 2);
		//assertEquals(testProp.getProperty("greeting"), "Hello, Zemian Deng.");
	}
}