/**
 * 
 */
package deng.javademo.util;

import junit.framework.Assert;

import org.junit.Test;

/**
 * @author Zemian Deng
 *
 * Created on Nov 12, 2009
 */
public class CounterTest {
	@Test
	public void testInc() {
		Counter counter = new Counter();
		Assert.assertEquals(0, counter.getCount());
		
		counter.inc();
		Assert.assertEquals(1, counter.getCount());
		
		for(int i=0; i< 9; i++) {
			counter.inc();
		}
		Assert.assertEquals(10, counter.getCount());
		
		counter.reset();
		Assert.assertEquals(0, counter.getCount());
	}
}
