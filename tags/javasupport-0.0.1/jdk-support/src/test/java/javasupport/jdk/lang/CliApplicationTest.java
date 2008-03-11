/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package javasupport.jdk.lang;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author thebugslayer
 */
public class CliApplicationTest extends CliApplication {
       
    /*@Test public void testPrintlnAndExit(String[] args) {
    //screen test with human interaction b/c exit and println can't be automated.
    if(args.length>0 && args[0].equals("-h")){
    exit("Usage MyJavaDemo [arguments]"); // run this with -h option to test exit()
    }		
    println("Running Helper class unit tests."); //should see this on screen!
    }*/
    @Test
    public void parseEmptyOptions() {
        String[] commands = new String[]{};
        Object[] args = parseOptions(commands);
        Assert.assertEquals(Collections.EMPTY_MAP, opts);
        Assert.assertEquals(true, Arrays.equals(new String[]{}, args));
        Assert.assertEquals(false, hasOpt("d"));
        Assert.assertEquals(false, getBooleanOpt("d", false));
        Assert.assertEquals(true, getBooleanOpt("d", true));
    }

    @Test
    public void parseShortOptions() {
        String[] commands = new String[]{"-d"};
        Object[] args = parseOptions(commands);
        Assert.assertEquals(1, opts.size());
        Assert.assertEquals(true, hasOpt("d"));
        Assert.assertEquals(true, getBooleanOpt("d"));
        Assert.assertEquals(true, Arrays.equals(new String[]{}, args));

        commands = new String[]{"-d", "-fFILE1.txt"};
        args = parseOptions(commands);
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, hasOpt("d"));
        Assert.assertEquals(true, hasOpt("f"));
        Assert.assertEquals("FILE1.txt", getOpt("f"));
        Assert.assertEquals(true, Arrays.equals(new String[]{}, args));

        commands = new String[]{"-d", "a1", "-fFILE1.txt", "a2", "a3"};
        args = parseOptions(commands);
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, hasOpt("d"));
        Assert.assertEquals(true, hasOpt("f"));
        Assert.assertEquals("FILE1.txt", getOpt("f"));
        Assert.assertEquals(3, args.length);
        Assert.assertEquals("a1", args[0]);
        Assert.assertEquals("a2", args[1]);
        Assert.assertEquals("a3", args[2]);
    }

    @Test
    public void parseLongOptions() {
        String[] commands = new String[]{"--debug"};
        Object[] args = parseOptions(commands);
        Assert.assertEquals(1, opts.size());
        Assert.assertEquals(true, hasOpt("debug"));
        Assert.assertEquals(true, getBooleanOpt("debug"));
        Assert.assertEquals(true, Arrays.equals(new String[]{}, args));

        //NOTE: NO space is allow between equals char.
        commands = new String[]{"--debug", "--file=FILE1.txt"};
        args = parseOptions(commands);
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, hasOpt("debug"));
        Assert.assertEquals(true, hasOpt("file"));
        Assert.assertEquals("FILE1.txt", getOpt("file"));
        Assert.assertEquals(true, Arrays.equals(new String[]{}, args));

        commands = new String[]{"--debug", "a1", "--file=FILE1.txt", "a2", "a3"};
        args = parseOptions(commands);
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, hasOpt("debug"));
        Assert.assertEquals(true, hasOpt("file"));
        Assert.assertEquals("FILE1.txt", getOpt("file"));
        Assert.assertEquals(3, args.length);
        Assert.assertEquals("a1", args[0]);
        Assert.assertEquals("a2", args[1]);
        Assert.assertEquals("a3", args[2]);
    }
    
    public static class Hello extends CliApplication {
        void main(String[] args) { 
            args = parseOptions(args);
            if (hasOpt("help")) {
                exit("java Hello [options] arguments...");
            }        
            if (hasOpt("file")) {
                println("We will write to file " + getOpt("file", "test.txt"));
            } 
            if (hasOpt("maxThread")) {
                println("We will set limit of " + getIntOpt("maxThread", 3) + " threads per run");
            }
            println("Hello, you have " + args.length + " argument(s) after options parsing.");
        }
    }
} 
