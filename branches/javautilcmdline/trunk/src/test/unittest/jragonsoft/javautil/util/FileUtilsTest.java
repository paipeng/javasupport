package jragonsoft.javautil.util;
import java.util.*;
import java.io.*;
import junit.framework.*;

/**
 *  Description of the Class
 *
 *@author     zemian
 *@version    $Id: FileUtilsTest.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FileUtilsTest extends TestCase {
	File testDir, textFile, binaryFile;
	
	/**  The JUnit setup method */
	protected void setUp() {
		testDir = new File("build/test/javautil-test_" + System.currentTimeMillis());
		testDir.mkdirs();
		if(!testDir.isDirectory()){
			throw new RuntimeException("Can not setup test dir.");
		}
		
		try{
			textFile = new File(testDir, "Text" + System.currentTimeMillis() + ".txt");
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(textFile)));
			out.println("This is a text file for FileUtils unit test.");
			out.println("Filename: " + textFile.getAbsolutePath());
			out.println("Java System properties: " + System.getProperties().toString());
			out.flush();
			out.close();
			
			binaryFile = new File(testDir, "Binary" + System.currentTimeMillis() + ".dat");
			ObjectOutputStream out2 = new ObjectOutputStream(new FileOutputStream(binaryFile));
			out2.writeObject(new Date());
			out2.writeDouble(1234.5678);
			out2.flush();
			out2.close();			
		}catch(Exception e){
			throw new RuntimeException("Can not setup test files.");
		}
	}


	/**  The teardown method for JUnit */
	protected void tearDown() {
		if(testDir.exists()){
			removeDirTree(testDir);
		}
	}
	
	void removeDirTree(File file){
		if(file.isDirectory()){
			File[] children = file.listFiles();
			for(int i = 0, maxIndex = children.length; i < maxIndex; i++){
				removeDirTree(children[i]);
			}
			file.delete();
		}else{
			file.delete();
		}
	}


	/**  A unit test for JUnit */
	public void testGetPathname() {
		assertEquals("", FileUtils.getPathname("one.txt"));
		assertEquals("tmp" + System.getProperty("file.separator") + "test", FileUtils.getPathname("tmp"+System.getProperty("file.separator")+"test"+System.getProperty("file.separator")+"two.txt"));
		assertEquals("..", FileUtils.getPathname(".."+System.getProperty("file.separator")+"three.txt"));
		assertEquals("", FileUtils.getPathname(""));
		assertEquals("", FileUtils.getPathname("     "));
	}


	/**  A unit test for JUnit */
	public void testGetBasename() {
		assertEquals("one.txt", FileUtils.getBasename("one.txt"));
		assertEquals("two.txt", FileUtils.getBasename("tmp"+System.getProperty("file.separator")+"test"+System.getProperty("file.separator")+"two.txt"));
		assertEquals("three.txt", FileUtils.getBasename(".."+System.getProperty("file.separator")+"three.txt"));
		assertEquals("", FileUtils.getBasename(""));
		assertEquals("     ", FileUtils.getBasename("     "));
	}

	public void testGetFilenameExt(){
		assertEquals("txt", FileUtils.getFilenameExt("test.txt"));
		assertEquals("", FileUtils.getFilenameExt("test"));
		assertEquals("", FileUtils.getFilenameExt("test."));
		assertEquals("pdf", FileUtils.getFilenameExt("/tmp/test.pdf"));
	}
	
	public void testGetFilenameWithoutExt(){
		assertEquals("test", FileUtils.getFilenameWithoutExt("test.txt"));
		assertEquals("test", FileUtils.getFilenameWithoutExt("test"));
		assertEquals("test", FileUtils.getFilenameWithoutExt("test."));
		assertEquals("/tmp/test", FileUtils.getFilenameWithoutExt("/tmp/test.pdf"));
	}

	public void testJoinPath(){
		assertEquals("tmp" + FileUtils.FILE_SEP + "test", 
			FileUtils.joinPath(new String[]{"tmp", "test"}));
		
		assertEquals("tmp" + FileUtils.FILE_SEP + "test" + FileUtils.FILE_SEP +"dir", 
			FileUtils.joinPath(new String[]{"tmp", "test", "dir"}));
	}

	public void testGetAbsPath(){
		try{
			assertTrue(FileUtils.getAbsPath(".").length() > 1);
			assertTrue(new File(".").getCanonicalPath().equals(new File(FileUtils.getAbsPath(".")).getCanonicalPath()));
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public void testIsSamePath(){
		assertTrue(FileUtils.isSamePath(new File(System.getProperty("user.home")), new File(System.getProperty("user.home"))));
		assertTrue(FileUtils.isSamePath(new File("/tmp/zemian"), new File("/tmp/zemian")));
	}

	public void testAppendBasename(){
		assertEquals("test.bak.txt", FileUtils.appendBasename("test.txt", ".bak"));
		assertEquals("test.txt", FileUtils.appendBasename("test.txt", ""));
		assertEquals("test.bak", FileUtils.appendBasename("test", ".bak"));
	}
	
	/**  A unit test for JUnit */
	public void testIsText() {
		assertTrue(FileUtils.isText(textFile));
		assertTrue(!FileUtils.isText(binaryFile));
	}


	/**  A unit test for JUnit */
	public void testTouchAndDelete() {
		File touchedFile = new File(testDir, "Touch" + System.currentTimeMillis());
		assertTrue(!touchedFile.exists());

		FileUtils.touch(touchedFile);
		assertTrue(touchedFile.exists());
	}


	/**  A unit test for JUnit */
	public void testMove() {
		File srcFile = new File(testDir, "MoveSrc" + System.currentTimeMillis());
		File destFile = new File(testDir, "MoveDest" + System.currentTimeMillis());

		FileUtils.touch(srcFile);
		assertTrue(srcFile.exists());
		assertTrue(!destFile.exists());

		FileUtils.move(srcFile, destFile);
		assertTrue(!srcFile.exists());
		assertTrue(destFile.exists());
	}


	/**  A unit test for JUnit */
	public void testCopy() {
		File srcFile = new File(testDir, "CopySrc" + System.currentTimeMillis());
		File destFile = new File(testDir, "CopyDest" + System.currentTimeMillis());

		FileUtils.touch(srcFile);
		assertTrue(srcFile.exists());
		assertTrue(!destFile.exists());

		FileUtils.copy(srcFile, destFile);

		assertTrue(srcFile.exists());
		assertTrue(destFile.exists());
	}


	/**  A unit test for JUnit */
	public void testWriteAppendAndReadLine() {
		File newFile = new File(testDir, "WriteAppend" + System.currentTimeMillis());

		assertTrue(!newFile.exists());

		FileUtils.writeLine(newFile, "test case 101");
		FileUtils.appendLine(newFile, "test case 102");
		FileUtils.appendLine(newFile, "test case 103");
		FileUtils.appendLine(newFile, "8080");
		FileUtils.appendLine(newFile, "3.14");
		FileUtils.appendLine(newFile, "true");

		assertEquals("test case 101", FileUtils.getLine(newFile, 1));
		assertEquals("test case 103", FileUtils.getLine(newFile, 3));
		assertEquals("test case 102", FileUtils.getLine(newFile, 2));
		assertEquals(8080, FileUtils.getInt(newFile, 4));
		assertEquals("3.14", "" + FileUtils.getDouble(newFile, 5));
		assertEquals(true, FileUtils.getBoolean(newFile, 6));

		String[] lines = FileUtils.getLines(newFile);
		assertEquals(6, lines.length);
		assertEquals("test case 101", lines[0]);
		assertEquals("test case 102", lines[1]);
		assertEquals("test case 103", lines[2]);
		assertEquals("8080", lines[3]);
		assertEquals("3.14", lines[4]);
		assertEquals("true", lines[5]);
	}


	/**  A unit test for JUnit */
	public void testWriteGetString() {
		File newFile = new File(testDir, "WriteGetString" + System.currentTimeMillis());

		assertTrue(!newFile.exists());

		String text = ""
				 + "[junit] Testcase: testGetPathname took 0.011 sec               \n"
				 + "[junit] Testcase: testGetBasename took 0.001 sec               \n"
				 + "[junit] Testcase: testIsText took 0.014 sec                    \n"
				 + "[junit] Testcase: testTouchAndDelete took 0.003 sec            \n"
				 + "[junit] Testcase: testMove took 0.015 sec                      \n"
				 + "[junit] Testcase: testCopy took 0.002 sec                      \n"
				 + "[junit] Testcase: testWriteAppendAndReadLine took 0.013 sec    \n"
				 + "[junit] Testcase: testGetString took 0 sec                     \n";
		FileUtils.writeString(newFile, text);
		assertEquals(text, FileUtils.getString(newFile));
	}


	/**  A unit test for JUnit */
	public void testGetBytesAndLength() {
		File newFile = new File(testDir, "GetByteLength" + System.currentTimeMillis());
		assertTrue(!newFile.exists());

		String text = ""
				 + "[junit] Testcase: testGetPathname took 0.011 sec               \n"
				 + "[junit] Testcase: testGetBasename took 0.001 sec               \n"
				 + "[junit] Testcase: testIsText took 0.014 sec                    \n"
				 + "[junit] Testcase: testTouchAndDelete took 0.003 sec            \n"
				 + "[junit] Testcase: testMove took 0.015 sec                      \n"
				 + "[junit] Testcase: testCopy took 0.002 sec                      \n"
				 + "[junit] Testcase: testWriteAppendAndReadLine took 0.013 sec    \n"
				 + "[junit] Testcase: testGetString took 0 sec                     \n";
		FileUtils.writeString(newFile, text);
		byte[] data = FileUtils.getBytes(newFile);
		assertEquals(text.length(), data.length);
		assertEquals(text, new String(data));
		assertEquals(data.length, FileUtils.getLength(newFile));
	}

	/**  A unit test for JUnit */
	public void testGlobFiles() {
		File newFile1 = new File(testDir, "GlobFile1_yoyo_" + System.currentTimeMillis());
		File newFile2 = new File(testDir, "GlobFile2_yoyo_" + System.currentTimeMillis());
		File newFile3 = new File(testDir, "GlobFile3_yoyo_" + System.currentTimeMillis());
		assertTrue(!newFile1.exists());
		assertTrue(!newFile2.exists());
		assertTrue(!newFile3.exists());

		FileUtils.touch(newFile1);
		FileUtils.touch(newFile2);
		FileUtils.touch(newFile3);

		File[] files = FileUtils.globFiles(testDir, "^GlobFile\\d+_yoyo");
		assertTrue(files.length >= 3);
	}


	/**  A unit test for JUnit */
	public void testGlobDirs() {
		File newFile1 = new File(testDir, "GlobDir1_yoyo_" + System.currentTimeMillis());
		File newFile2 = new File(testDir, "GlobDir2_yoyo_" + System.currentTimeMillis());
		File newFile3 = new File(testDir, "GlobDir3_yoyo_" + System.currentTimeMillis());
		assertTrue(!newFile1.exists());
		assertTrue(!newFile2.exists());
		assertTrue(!newFile3.exists());

		newFile1.mkdirs();
		newFile2.mkdirs();
		newFile3.mkdirs();

		File[] files = FileUtils.globDirs(testDir, "^GlobDir\\d+_yoyo");
		assertTrue(files.length >= 3);
	}
}

