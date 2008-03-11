package ${groupId};

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Main application entry point.
 * To run it with maven, do: mvn exec:java -Dexec.mainClass=deng.Main
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Object[] optsResult = parseOptions(args);
        Properties opts = (Properties)optsResult[0];        
        
        if(opts.containsKey("d")){
            println("Original arguments:");
            for(String s : args) println(s);

            println("After options arguments:");
            List<String> newArgs = (List<String>)optsResult[1];
            for(String s : newArgs) println(s);

            println("Options:");
            println(opts.toString());
        }
        
        if(opts.containsKey("h")){
            println("Usage: Main [options]");
            println("  -d   Display option parsing info.");
            println("  -h   Display help page.");
            println("  -i  Display system information.");
        }else if(opts.containsKey("i")){
            printSystemInfo();            
        }else{
            println("This java application is ready. Type -help optiont to see more.");
        }
    }
    
    public static void printSystemInfo(){          
        println("System information");
        printf("%15s: %s\n", "HOME", System.getProperty("user.home"));
        printf("%15s: %s\n", "OS", System.getProperty("os.name") + " " + System.getProperty("os.arch") + " " + System.getProperty("os.version"));
        printf("%15s: %s\n", "Java", System.getProperty("java.vm.name") + " " + System.getProperty("java.vm.version"));

        //Calculate system resources.
        int MB = 1024 * 1024;
        Runtime runtime = Runtime.getRuntime();
        long totalMem = runtime.totalMemory() / MB;
        long freeMem = runtime.freeMemory() / MB;
        long avaCPU = runtime.availableProcessors();
        printf("%15s: %d\n", "CPU", avaCPU);
        printf("%15s: %s\n", "MemoryUsed", (totalMem - freeMem) + "M/" + totalMem + "M");
    }
    
    public static void println(String msg) {
        System.out.println(msg);
    }

    public static void printf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /** 
     * Simple parser for short and long style command line options and arguments. 
     * Short option format is single dash prefix with single char flag. Anything beyong
     * the first char will be treated as parameter value for it's flag.
     * Long option format is two dashes prefix with one or more chars flag. Option parameter
     * must specify with equal char.
     * @return Object[] will always contain two elements tuple. [0]=options, [1]=newArgumentList.
     */
    public static Object[] parseOptions(String[] args) {
        Properties opts = new Properties();
        List argsList = new ArrayList();
        for (int i = 0,  maxIndex = args.length; i < maxIndex; i++) {
            String arg = args[i];
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
                argsList.add(arg);
            }
        }
        return new Object[]{opts, argsList};
    }
}

