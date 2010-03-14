package javasupport.jdk.lang;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Properties;

import org.apache.commons.lang.SystemUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test cases for Helper class. To keep things simple, we only going to use Assert.assertEquals from
 * the Helper class for testing. No external library is required.
 * 
 * @author Zemian Deng
 * @since Wed Feb 20 00:39:43 EST 2008
 */
public class ShortMethodsHelperTest extends ShortMethodsHelper {
	String testDir = System.getProperty("java.io.tmpdir");
	
    @Test
    public void testSlice() {
        Assert.assertEquals(mklist(0, 1, 2, 3, 4, 5), slice(mklist(0, 1, 2, 3, 4, 5), 0, -1)); //all
        Assert.assertEquals(mklist(5), slice(mklist(0, 1, 2, 3, 4, 5), -1, -1)); //last
        Assert.assertEquals(mklist(0), slice(mklist(0, 1, 2, 3, 4, 5), 0, 0)); //first
        Assert.assertEquals(mklist(0, 1, 2, 3, 4), slice(mklist(0, 1, 2, 3, 4, 5), 0, -2)); //head
        Assert.assertEquals(mklist(1, 2, 3, 4, 5), slice(mklist(0, 1, 2, 3, 4, 5), 1, -1)); //tail

        Assert.assertEquals(mklist(1, 2, 3, 4), slice(mklist(0, 1, 2, 3, 4, 5), 1, 4)); //middle
        Assert.assertEquals(mklist(1, 3), slice(mklist(0, 1, 2, 3, 4, 5), 1, 4, 2)); //step of 2
    }

    @Test
    public void testRegex() {
        Assert.assertEquals(2, regexMatches("(\\d+) (\\d+)", "123 456").groupCount());
        Assert.assertEquals("123 456", regexMatches("(\\d+) (\\d+)", "123 456").group(0));
        Assert.assertEquals("123", regexMatches("(\\d+) (\\d+)", "123 456").group(1));
        Assert.assertEquals("456", regexMatches("(\\d+) (\\d+)", "123 456").group(2));
        try {
            Assert.assertEquals(0, regexMatches("(\\d+) (\\d+)", "abc 123 456 efg").groupCount());
            throw new RuntimeException("There should not be a match here.");
        } catch (Exception e) { /*expected.*/ }

        Assert.assertEquals(2, regexFind("(\\d+) (\\d+)", "abc 123 456 efg").groupCount());
        Assert.assertEquals("123 456", regexFind("(\\d+) (\\d+)", "abc 123 456 efg").group(0));
        Assert.assertEquals("123", regexFind("(\\d+) (\\d+)", "abc 123 456 efg").group(1));
        Assert.assertEquals("456", regexFind("(\\d+) (\\d+)", "abc 123 456 efg").group(2));
        try {
            Assert.assertEquals(0, regexFind("(\\d+) (\\d+)", "123 abc 456 efg").groupCount());
            throw new RuntimeException("There should not be a match here.");
        } catch (Exception e) { /*expected.*/ }
    }

    @Test
    public void testExec() {
        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.indexOf("linux") >= 0 || osname.indexOf("mac") >= 0) {
            //Echo cmd test
            Assert.assertEquals("self\n", exec("echo", "self"));
            Assert.assertEquals("one two three\n", exec("echo", "one", "two", "three"));

            //just a fake cmd for echo.
            String params = "tar cvf /data/backup/myarchive.tgz /opt/myapp/reports/*.txt";
            Assert.assertEquals(params + "\n", exec("echo", params));

            //Unix tar cmd test
            String file1 = testDir + "/test.tgz";
            String file2 = testDir + "/test1.txt";
            String file3 = testDir + "/test2.txt";

            writeText(file2, "file one");
            writeText(file3, "file two");
            Assert.assertEquals(true, new File(file2).exists());
            Assert.assertEquals(true, new File(file3).exists());

            exec("tar", "cvf", file1, file2, file3);
            Assert.assertEquals(true, new File(file1).exists());
            Assert.assertEquals(true, new File(file1).length() > 0);


            String[] result = exec("tar", "tvf", file1).split("\n");
            Assert.assertEquals(2, result.length);
            String[] filenames1 = result[0].split(" ");
            Assert.assertEquals("tmp/test1.txt", filenames1[filenames1.length - 1]);
            String[] filenames2 = result[1].split(" ");
            Assert.assertEquals("tmp/test2.txt", filenames2[filenames2.length - 1]);

            new File(file1).delete();
            new File(file2).delete();
            new File(file3).delete();
        }
    }

    @Test
    public void testSprintf() {
        Assert.assertEquals("99 bottles of bears", sprintf("%d bottles of %s", 99, "bears"));
    }

    @Test
    public void testDates() throws Exception {
        Date bd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1977-9-17 20:00:00");
        Assert.assertEquals(bd, mkdatetime("yyyy-MM-dd HH:mm:ss", "1977-9-17 20:00:00"));

        bd = new SimpleDateFormat("yyyy-MM-dd").parse("1977-9-17");
        Assert.assertEquals(bd, mkdate("1977-9-17"));
        bd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("1977-9-17 20:00:00");
        Assert.assertEquals(bd, mkdatetime("1977-9-17 20:00:00"));

        Assert.assertEquals("19770917203040", datef("yyyyMMddHHmmss", mkdatetime("1977-9-17 20:30:40")));
    }

    @Test
    public void testJoin() {
        //single char sep
        Assert.assertEquals("one", join(mklist("one"), "|")); //single item will not include sep.
        Assert.assertEquals("one|two", join(mklist("one", "two"), "|"));
        Assert.assertEquals("one|two|three", join(mklist("one", "two", "three"), "|"));

        //long chars sep
        Assert.assertEquals("one", join(mklist("one"), "***")); //single item will not include sep.
        Assert.assertEquals("one***two", join(mklist("one", "two"), "***"));
        Assert.assertEquals("one***two***three", join(mklist("one", "two", "three"), "***"));
    }

    @Test
    public void testFileReadWrite() {
        //test file read and write & mklist
        String filename = testDir + "/test.txt";

        //test empty file
        writeText(filename, "");
        Assert.assertEquals("", readText(filename));

        //test single line
        writeText(filename, "one\n");
        Assert.assertEquals("one\n", readText(filename));

        //test multi-lines
        writeLines(filename, mklist("one\n", "two\n", "three\n"));
        Assert.assertEquals(mklist("one", "two", "three"), readLines(filename));

        //remove temp file
        new File(filename).delete();
    }

}
