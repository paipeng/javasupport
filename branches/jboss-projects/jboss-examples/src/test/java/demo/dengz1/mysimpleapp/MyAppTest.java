package demo.dengz1.mysimpleapp;

import java.util.Properties;

import junit.framework.Assert;

import org.junit.Test;

public class MyAppTest {	
	@Test
	public void getProps() {
		Properties props = MyApp.getProps();		
		Assert.assertTrue("Size must greater than one.", props.size() > 1);
	}
}
