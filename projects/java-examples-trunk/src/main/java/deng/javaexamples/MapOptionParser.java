package deng.javaexamples;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapOptionParser {
	public static void main(String[] argv) {
		Options options = new Options().parse(argv);
		List<String> args = options.getArgs();
		Map<String, String> opts = options.getOpts();
		
		System.out.println("Original Arguments: " + Arrays.asList(argv));
		System.out.println("Parsed Argumetns: " + args);
		System.out.println("Parsed Options: " + opts);
	}
	
	/** A very simple command line argument parser. */
	public static class Options {
		private List<String> args = new ArrayList<String>();
		private Map<String, String> opts = new HashMap<String, String>();
		
		public boolean has(String name) {
			return opts.containsKey(name);
		}
		public String get(String name) {
			return opts.get(name);
		}
		
		public Options parse(String[] argv) {
			for(int i = 0, max = argv.length; i < max; i++) {
				String optionStr = null;
				String arg = argv[i];
				//System.out.println("argv_" + i + " >" + arg);
				if(arg.equals("--")) { // escape all options parsing from here on.
					for(int j = i + 1; j < argv.length; j++)
						args.add(argv[j]);
					break; // break out of all arguments parsing!
				} else if(arg.startsWith("--")) {
					optionStr = arg.substring(2);
				} else if(arg.startsWith("-")) {
					optionStr = arg.substring(1);
				} else {
					args.add(arg);
					continue;
				}
				
				//Process the option value.
				String flag = optionStr;
				String value = "true"; //default value for any option if no value is set.
				int valuePos = optionStr.indexOf("=");
				if(valuePos > 0) {
					flag = optionStr.substring(0, valuePos);
					value = optionStr.substring(valuePos + 1);
				}
				opts.put(flag, value);
			}
			return this;
		}
		
		public List<String> getArgs() {
			return args;
		}
		public Map<String, String> getOpts() {
			return opts;
		}
	}
}
