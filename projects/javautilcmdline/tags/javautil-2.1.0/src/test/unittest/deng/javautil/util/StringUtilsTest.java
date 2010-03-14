package deng.javautil.util;

import java.util.*;
import junit.framework.*;

public class StringUtilsTest extends TestCase{
	
	protected void setUp(){
	}	
	protected void tearDown(){
	}
	
	public void testIsNumber(){
		assertTrue(StringUtils.isNumber("89999"));
		assertTrue(StringUtils.isNumber("0"));
		assertTrue(StringUtils.isNumber("001"));
		assertTrue(!StringUtils.isNumber("A1A"));
		assertTrue(!StringUtils.isNumber("-1"));
	}
	
	public void testReplace(){
		assertEquals("%s", StringUtils.replace("%%s", "%%", "%"));
		assertEquals("Dog is better than cat.", StringUtils.replace("Bird is better than cat.", 
				"Bird", "Dog"));
		assertEquals("Bird is not better than cat.", StringUtils.replace("Bird is better than cat.", 
				"is", "is not"));
				
		assertEquals("None", StringUtils.replace("None","$", "XX"));
		assertEquals("go one two go three", StringUtils.replace("#{K} one two #{K} three","#{K}", "go"));
		
	}
	
	public void testTrim(){
		assertEquals("test", StringUtils.trimFront(" test", " "));
		assertEquals("test", StringUtils.trimFront("test", " "));	
		assertEquals("test", StringUtils.trimBack("test", " "));
		assertEquals("test", StringUtils.trimBack("test ", " "));
		assertEquals("test", StringUtils.trimBack("test", " "));
	}
	
	public void testParseNumber(){
		assertEquals(0 , StringUtils.toInt("000"));
		assertEquals(1 , StringUtils.toInt("   1"));	
		assertEquals(2 , StringUtils.toInt("2   "));	
		assertEquals(3 , StringUtils.toInt("   3   "));	
		
		assertEquals("" +0.0 , "" + StringUtils.toDouble("000"));
		assertEquals("" +1.0 , "" + StringUtils.toDouble("   1"));	
		assertEquals("" +2.0 , "" + StringUtils.toDouble("2   "));	
		assertEquals("" +3.0 , "" + StringUtils.toDouble("   3   "));	
	}
	
	public void testIsBlank(){
		assertTrue(StringUtils.isBlank(null));
		assertTrue(StringUtils.isBlank(""));
		assertTrue(StringUtils.isBlank(" "));
		assertTrue(StringUtils.isBlank("   "));
		assertTrue(StringUtils.isNotBlank("-"));
	}
	
	public void testJoin(){
		assertEquals("", StringUtils.join(" ", new String[]{null}));
		assertEquals(" ", StringUtils.join(" ", new String[]{null, null}));
		assertEquals("", StringUtils.join(" ", new String[0]));
		assertEquals("A", StringUtils.join(" ", new String[]{"A"}));
		assertEquals(" A", StringUtils.join(" ", new String[]{"", "A"}));
		assertEquals("A B", StringUtils.join(" ", new String[]{"A", "B"}));
		
		TreeMap p = new TreeMap();
		p.put("A", "a");
		p.put("B", "b");
		p.put("C", "c");
		assertEquals("A=a, B=b, C=c", StringUtils.join(p, ", ", "="));
	}

	public void testSplit(){
		//assertEquals(1, StringUtils.split(null, " ").length);
		//assertTrue(Arrays.equals(new String[]{""}, StringUtils.split(null, " ")));	
		
		assertEquals(1, StringUtils.split("A", null).length);
		assertTrue(Arrays.equals(new String[]{"A"}, StringUtils.split("A", null)));	
		
		assertEquals(1, StringUtils.split("", " ").length);
		assertTrue(Arrays.equals(new String[]{""}, StringUtils.split("", " ")));	
		
		assertEquals(1, StringUtils.split("", " ").length);
		assertTrue(Arrays.equals(new String[]{""}, StringUtils.split("", " ")));	
		
		assertEquals(2, StringUtils.split("A ", " ").length);
		assertTrue(Arrays.equals(new String[]{"A", ""}, StringUtils.split("A ", " ")));
		
		assertEquals(2, StringUtils.split(" A", " ").length);
		assertTrue(Arrays.equals(new String[]{"", "A"}, StringUtils.split(" A", " ")));
		
		assertEquals(3, StringUtils.split(" A ", " ").length);
		assertTrue(Arrays.equals(new String[]{"", "A", ""}, StringUtils.split(" A ", " ")));
		
		assertEquals(3, StringUtils.split("A  C", " ").length);
		assertTrue(Arrays.equals(new String[]{"A", "", "C"}, StringUtils.split("A  C", " ")));
		
		assertEquals(3, StringUtils.split("A B C", " ").length);
		assertTrue(Arrays.equals(new String[]{"A", "B", "C"}, StringUtils.split("A B C", " ")));
		
		String test ="file:///System/Library/Frameworks/JavaVM.framework/Versions/1.4.2/Resources/Documentation/Reference/doc/api/java/util/Arrays.html";
		String[] parts = {"file:","","", "System", "Library", "Frameworks", "JavaVM.framework", "Versions", "1.4.2", "Resources", "Documentation", "Reference", "doc", "api", "java", "util", "Arrays.html"};
		String[] parts2 = {"file:","","", "System", "Library", "Frameworks", "JavaVM.framework", "Versions", "1.4.2", "Resources", "Documentation/Reference/doc/api/java/util/Arrays.html"};
		assertEquals(17, StringUtils.split(test, "/").length);
		assertTrue(Arrays.equals(parts, StringUtils.split(test, "/")));
		
		assertEquals(11, StringUtils.split(test, "/", 10).length);
		assertTrue(Arrays.equals(parts2, StringUtils.split(test, "/", 10)));
	}
	
	public void testToHexString(){
		byte[] data = { (byte)0x00, (byte)0x0F, (byte)0xFF, (byte)0x10, 
			(byte)0x01, (byte)0xAA, (byte)0xBB, (byte)0xCC };
		String hex = StringUtils.toHexString(data);
		assertEquals("000FFF1001AABBCC", hex);
		
		hex = StringUtils.toHexString(data, 0, 4, 2);
		assertEquals("00 0F"+System.getProperty("line.separator")+"FF 10", hex);
	}
	
	public void testPadString(){
		String[] tests ={ "1", "2002", "XX", "why" };
		
		assertEquals("    1", StringUtils.padFrontChar(tests[0], 5, ' '));
		assertEquals("    1", StringUtils.pad(tests[0], 5));
		assertEquals("1    ", StringUtils.padBackChar(tests[0], 5, ' '));
		assertEquals("2002", StringUtils.pad(tests[1], 4));
		assertEquals("2002", StringUtils.pad(tests[1], 3));
	}
	
	public void testSplitAndJoin(){
		String[] testStrings = {
			"bin, build, build.number, build.xml, doc, lib, src, test",
			"one",
			"",
			"one, , two",
			", one, "
		};
		for(int i = 0; i < testStrings.length; i++){
			String[] parts = StringUtils.split(testStrings[i], ", ");
			assertEquals(testStrings[i], StringUtils.join(", ", parts));
		}
	}
	
	public void testReaplace(){
		assertEquals("test ?me?", StringUtils.replace("test -me-", "-", "?"));
		assertEquals("X", StringUtils.replace("XX", "XX", "X"));
		assertEquals("$", StringUtils.replace("$$", "$$", "$"));		
		assertEquals("one|two|three", StringUtils.replace("one;two;three", ";", "|"));
		assertEquals("one;two;three", StringUtils.replace("one;two;three", ";", ";"));
	}
}
