import org.junit.Assert;
import org.junit.Test;


class StringUtilsTest {
	
	@Test
	def void testSplit() {
		def sutil = new StringUtils()
		def ret = sutil.splitStr("a b c d e f g", " ")
		Assert.assertEquals(['a', 'b', 'c', 'd', 'e', 'f', 'g'], ret)
		
		ret = sutil.splitStr2("a b c d e f g", " ")
		Assert.assertEquals(['a', 'b', 'c', 'd', 'e', 'f', 'g'], ret)
		
		ret = sutil.splitStr3("a b c d e f g", " ")
		Assert.assertEquals(['a', 'b', 'c', 'd', 'e', 'f', 'g'], ret)
	}
	
	/**
splitStr 0.219 ms
splitStr 0.157 ms
splitStr2 0.718 ms
splitStr2 0.531 ms
splitStr3 0.391 ms
splitStr3 0.312 ms
StringTest.split 0.032 ms
StringTest.split 0.015 ms
	 */
	@Test
	def void testSplitSpeed() {
		def sutil = new StringUtils()
		def t = 0
		def n = 10000
		def input = "a b c d e f g"
		def sep = " "
		
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr ${t / 1000} ms")
		
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr ${t / 1000} ms")
		
		//===========================
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr2(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr2 ${t / 1000} ms")
		
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr2(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr2 ${t / 1000} ms")
		
		//===========================
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr3(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr3 ${t / 1000} ms")
		
		t = System.currentTimeMillis()
		n.times { 
			def ret = sutil.splitStr3(input, sep)
		}
		t = System.currentTimeMillis() - t
		println("splitStr3 ${t / 1000} ms")
		
		//===========================
		t = System.currentTimeMillis()
		n.times { 
			def ret = StringTest.split(input, sep, -1)
		}
		t = System.currentTimeMillis() - t
		println("StringTest.split ${t / 1000} ms")
		
		t = System.currentTimeMillis()
		n.times { 
			def ret = StringTest.split(input, sep, -1)
		}
		t = System.currentTimeMillis() - t
		println("StringTest.split ${t / 1000} ms")
	}
}
