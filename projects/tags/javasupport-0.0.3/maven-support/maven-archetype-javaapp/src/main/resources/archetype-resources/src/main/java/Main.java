package ${groupId};

import javasupport.jdk.lang.CliApplication;

/**
 * Sample of simple CliApplication implementation.
 * To run it with maven that include all dependecies, type: 
 *   mvn exec:java -Dexec.mainClass=${groupId}.Main -Dexec.args="--debug --help"
 */
public class Main extends CliApplication{

    public static void main(String[] argv) {
        String[] args = parseOptions(argv);
        
        if(hasOpt("d") || hasOpt("debug")){
            println("Original arguments:");
            for(String s : argv) println(s);

            println("After options arguments:");
            for(String s : args) println(s);

            println("Options:");
            println(opts.toString());
        }
        
        if(hasOpt("d") || hasOpt("help")){
            println("Usage: Main [options] <arguments...>");
            println("  -d, --debug   Display option parsing info.");
            println("  -h, --help     Display help page.");
            println("  -i, --sysinfo  Display system information.");
            exit("-- end " + now());
        }else if(hasOpt("i") || hasOpt("sysinfo")){
            printSystemInfo();    
            exit("-- end " + now());
        }
        
        println("This java application is ready. Type --help optiont to see more.");
        println("-- end " + now());
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
}

