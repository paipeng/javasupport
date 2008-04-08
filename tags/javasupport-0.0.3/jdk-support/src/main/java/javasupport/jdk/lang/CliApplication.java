/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package javasupport.jdk.lang;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.apache.commons.collections.MapUtils;

/**
 * Provide convient method ready for Subclass to parse
 * Command line interface options and arguments. A typical usages example:
 * 
 * <pre>
 *   public class Hello extends CliApplication {
 *     public static void main(String[] args) {
 *       args = parseOptions(args);
 *       if(hasOpt("help")){
 *           exit("java Hello [options] arguments...");
 *       }
 *       if (hasOpt("file")) {
 *           println("We will write to file " + getOpt("file", "test.txt"));
 *       } 
 *       if (hasOpt("maxThread")) {
 *           println("We will set limit of " + getIntOpt("maxThread", 3) + " threads per run");
 *       }
 *       println("Hello, you have " + args.length + " argument(s) after options parsing.");
 *     }
 *   } 
 * </pre>
 *
 * @author thebugslayer
 */
public class CliApplication extends ShortMethodsHelper{
    protected static Properties opts;
    
    protected static boolean hasOpt(String key){ return opts.containsKey(key); }
    protected static String getOpt(String key){ return opts.getProperty(key); }    
    protected static String getOpt(String key, String def){ return opts.getProperty(key, def); }
    protected static boolean getBooleanOpt(String key){ return MapUtils.getBooleanValue(opts, key); }    
    protected static boolean getBooleanOpt(String key, boolean def){ return MapUtils.getBooleanValue(opts, key, def); }
    protected static int getIntOpt(String key){ return MapUtils.getIntValue(opts, key); }    
    protected static int getIntOpt(String key, int def){ return MapUtils.getIntValue(opts, key, def); }
    protected static double getDoubleOpt(String key){ return MapUtils.getDoubleValue(opts, key); }    
    protected static double getDoubleOpt(String key, double def){ return MapUtils.getDoubleValue(opts, key, def); }
        
    /** 
     * Simple parser for short and long style command line options and arguments. 
     * Short option format is single dash prefix with single char flag. Anything beyong
     * the first char will be treated as parameter value for it's flag.
     * Long option format is two dashes prefix with one or more chars flag. Option parameter
     * must specify with equal char.
     * 
     * @return array of argument after options parsing.
     */
    protected static String[] parseOptions(String[] mainArgs) {
        opts = new Properties();
        List<String> args = new ArrayList<String>();
        for (int i = 0,  maxIndex = mainArgs.length; i < maxIndex; i++) {
            String arg = mainArgs[i];
            if (arg.startsWith("--")) {
                String[] s = arg.substring(2).split("=");
                if (s.length >= 2) {
                    opts.setProperty(s[0], s[1]);
                } else {
                    opts.setProperty(s[0], "true");
                }
            } else if (arg.startsWith("-")) {
                String s = arg.substring(1);
                if (s.length() > 1) {
                    opts.setProperty(s.substring(0, 1), s.substring(1));
                } else {
                    opts.setProperty(s, "true");
                }
            } else {
                args.add(arg);
            }
        }
        return (String[])args.toArray(new String[args.size()]);
    }
}
