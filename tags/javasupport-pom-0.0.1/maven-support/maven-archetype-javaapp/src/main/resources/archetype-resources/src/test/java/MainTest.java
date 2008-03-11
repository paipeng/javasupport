package ${groupId};

import java.util.Collections;
import java.util.List;
import java.util.Properties;
import org.junit.Test;
import org.junit.Assert;

public class MainTest {
    @Test public void parseEmptyOptions(){
        String[] commands = new String[]{};
        Object[] ret = Main.parseOptions(commands);
        Assert.assertEquals(2, ret.length);
        Assert.assertEquals(Collections.EMPTY_MAP, ret[0]);
        Assert.assertEquals(Collections.EMPTY_LIST, ret[1]);
    }
    
    @Test public void parseShortOptions(){
        String[] commands = new String[]{"-d"};
        Object[] ret = Main.parseOptions(commands);
        Properties opts = (Properties)ret[0];
        List<String> args = (List<String>)ret[1];
        
        Assert.assertEquals(1, opts.size());
        Assert.assertEquals(true, opts.containsKey("d"));
        Assert.assertEquals("true", opts.getProperty("d"));
        Assert.assertEquals(Collections.EMPTY_LIST, args);
        
        commands = new String[]{"-d", "-fFILE1.txt"};
        ret = Main.parseOptions(commands);
        opts = (Properties)ret[0];
        args = (List<String>)ret[1];
        
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, opts.containsKey("d"));
        Assert.assertEquals("true", opts.getProperty("d"));
        Assert.assertEquals(true, opts.containsKey("f"));
        Assert.assertEquals("FILE1.txt", opts.getProperty("f"));
        Assert.assertEquals(Collections.EMPTY_LIST, args);
        
        commands = new String[]{"-d", "a1", "-fFILE1.txt", "a2", "a3"};
        ret = Main.parseOptions(commands);
        opts = (Properties)ret[0];
        args = (List<String>)ret[1];
        
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, opts.containsKey("d"));
        Assert.assertEquals("true", opts.getProperty("d"));
        Assert.assertEquals(true, opts.containsKey("f"));
        Assert.assertEquals("FILE1.txt", opts.getProperty("f"));
        Assert.assertEquals(3, args.size());
        Assert.assertEquals("a1", args.get(0));
        Assert.assertEquals("a2", args.get(1));
        Assert.assertEquals("a3", args.get(2));
    }
    
    
    @Test public void parseLongOptions(){
        String[] commands = new String[]{"--debug"};
        Object[] ret = Main.parseOptions(commands);
        Properties opts = (Properties)ret[0];
        List<String> args = (List<String>)ret[1];
        
        Assert.assertEquals(1, opts.size());
        Assert.assertEquals(true, opts.containsKey("debug"));
        Assert.assertEquals("true", opts.getProperty("debug"));
        Assert.assertEquals(Collections.EMPTY_LIST, args);
        
        //NOTE: NO space is allow between equals char.
        commands = new String[]{"--debug", "--file=FILE1.txt"};
        ret = Main.parseOptions(commands);
        opts = (Properties)ret[0];
        args = (List<String>)ret[1];
        
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, opts.containsKey("debug"));
        Assert.assertEquals("true", opts.getProperty("debug"));
        Assert.assertEquals(true, opts.containsKey("file"));
        Assert.assertEquals("FILE1.txt", opts.getProperty("file"));
        Assert.assertEquals(Collections.EMPTY_LIST, args);
                
        commands = new String[]{"--debug", "a1", "--file=FILE1.txt", "a2", "a3"};
        ret = Main.parseOptions(commands);
        opts = (Properties)ret[0];
        args = (List<String>)ret[1];
        
        Assert.assertEquals(2, opts.size());
        Assert.assertEquals(true, opts.containsKey("debug"));
        Assert.assertEquals("true", opts.getProperty("debug"));
        Assert.assertEquals(true, opts.containsKey("file"));
        Assert.assertEquals("FILE1.txt", opts.getProperty("file"));
        Assert.assertEquals(3, args.size());
        Assert.assertEquals("a1", args.get(0));
        Assert.assertEquals("a2", args.get(1));
        Assert.assertEquals("a3", args.get(2));
    }
}
