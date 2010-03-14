package ztool;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CliHelper {
	/////////////////////////////////////////////////////////////////////////
	// Often used utility methods
	/////////////////////////////////////////////////////////////////////////
	public static void println(String msg) {
		System.out.println(msg);
	}
	public static void print(String msg) {
		System.out.print(msg);
	}
	public static void printf(String fmt, Object... params) {
		System.out.printf(fmt, params);
	}
	public static void exit(String msg) {
		println(msg);
		System.exit(-1);
	}
	public static void exit(Exception e) {
		e.printStackTrace();
		System.exit(-1);
	}
	public static List<String> matchGroups(String re, String input) {
		List<String> result = new ArrayList<String>();
		Pattern p = Pattern.compile(re);
		Matcher m = p.matcher(input);
		if (m.find()) {
			int max = m.groupCount();
			for (int i = 1; i <= max; i++) {
				result.add(m.group(i));
			}
		}
		return result;
	}
	
	/////////////////////////////////////////////////////////////////////////
	// File and text processing utility methods
	/////////////////////////////////////////////////////////////////////////
	public static void close(InputStream ins) {
		if (ins != null) {
			try {
				ins.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to close input stream.", e);
			}
		}
	}
	public static void close(OutputStream out) {
		if (out != null) {
			try {
				out.close();
			} catch (IOException e) {
				throw new RuntimeException("Failed to close output stream.", e);
			}
		}		
	}	
	public static interface InputStreamAction {
		public void onInputStream(InputStream ins);
	}
	public static void withInputStream(File file, InputStreamAction action) {
		FileInputStream fins = null;
		try {
			fins = new FileInputStream(file);
			BufferedInputStream bins = new BufferedInputStream(fins);
			action.onInputStream(bins);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + file);
		} finally {
			close(fins);
		}
	}
	public static interface LineAction {
		public void onLine(String line);
	}
	public static void eachLine(File file, LineAction action) {
		try {
			eachLine(new FileInputStream(file), action);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + file);
		}
	}
	public static void eachLine(InputStream ins, final LineAction action) {
		withReader(ins, new ReaderAction() {			
			//@Override
			public void onReader(BufferedReader reader) throws IOException {
				String line = null;
				while((line = reader.readLine()) != null) {
					action.onLine(line);
				}
			}
		});
	}
	public static interface ReaderAction {
		public void onReader(BufferedReader reader) throws IOException;
	}	
	public static void withReader(File file, ReaderAction action) {
		try {
			withReader(new FileInputStream(file), action);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + file);
		}
	}
	public static void withReader(InputStream ins, ReaderAction action) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(ins));
			action.onReader(reader);
		} catch(Exception e) {
			throw new RuntimeException("Failed to read from input stream.", e);
		} finally {
			if(reader != null)
				try {
					reader.close();
				} catch(Exception e) {
					throw new RuntimeException("Failed to close the reader.", e);
				} 
		}
	}
	
	public static interface WriterAction {
		public void onWriter(PrintWriter writer) throws IOException;
	}
	public static void withWriter(File file, WriterAction action) {
		try {
			withWriter(new FileOutputStream(file), action);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("File not found: " + file);
		}
	}
	public static void withWriter(OutputStream outs, WriterAction action) {
		PrintWriter writer = null;
		try {
			BufferedOutputStream bouts = new BufferedOutputStream(outs);
			writer = new PrintWriter(new OutputStreamWriter(bouts));
			action.onWriter(writer);
			writer.flush();
		} catch(Exception e) {
			throw new RuntimeException("Failed to write to output stream.", e);
		} finally {
			if(writer != null)
				writer.close();
		}
	}
	
	public static InputStream getInputStream(File file, InputStream defIfFileIsNull) {
		if (file == null) {
			return defIfFileIsNull;
		}		
		try {
			InputStream ins = new FileInputStream(file);
			return ins;
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String readText(InputStream ins) {
		final StringBuilder sb = new StringBuilder();
		final String sep = System.getProperty("line.separator");
		eachLine(ins, new LineAction() {
			public void onLine(String line) {
				sb.append(line + sep);				
			}
		});
		return sb.toString();
	}
	
	/////////////////////////////////////////////////////////////////////////
	// Options parsers
	/////////////////////////////////////////////////////////////////////////
	public static class Option {
		public Option(String flag, String valueFormat, String description) {
			this.flag = flag;
			this.valueFormat = valueFormat;
			this.description = description;
		}
		private String flag;
		private String value;
		private String valueFormat;
		private String description;
		public String getFlag() {
			return flag;
		}
		public String getDescription() {
			return description;
		}
		public String getValue() {
			return value;
		}
		public void setValue(String value) {
			this.value = value;
		}
		public String getValueFormat() {
			return valueFormat;
		}

		@Override
		public String toString() {
			return "Option(flag=" + flag + ", value=" + value + ")";
		}
	}
	

	public static class OptionsParser {		
		private Options options = new Options();
		public Options getOptions() {
			return options;
		}
		public Options parse(String[] argv) {
			Map<String, Option> optionsMap = options.getOptionsMap();
			Map<String, Option> extraOptionsMap = options.getExtraOptionsMap();
			
			// Start processing argv for options
			for(int i = 0, max = argv.length; i < max; i++) {
				String optionStr = null;
				String arg = argv[i];
				//println("processing arg: " + arg);
				
				if(arg.equals("--")) { // escape all options parsing from here on.
					for(int j = i + 1; j < argv.length; j++)
						options.args.add(argv[j]);
					break; // break out of all arguments parsing!
				} else if(arg.startsWith("--")) {
					optionStr = arg.substring(2);
				} else if(arg.startsWith("-")) {
					optionStr = arg.substring(1);
				} else {
					options.args.add(arg);
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
				
				//Find and save the option value.
				Option opt = optionsMap.get(flag);
				if (opt == null) {
					opt = extraOptionsMap.get(flag);
					if (opt == null) {
						throw new RuntimeException("Invalid option " + flag);
					}
				}
				opt.setValue(value);
			}
			//If help option is not added, go ahead and added it here.
			if (options.optionsMap.get("help") == null) {
				setOption("help", "Display this help info.");
			}
			
			return options;
		}
		
		public OptionsParser setUseShortFlag(boolean useShortFlag) {
			options.setUseShortFlag(useShortFlag);
			return this;
		}

		public OptionsParser setExamples(String examples) {
			options.setExamples(examples);
			return this;
		}
		public OptionsParser setSummary(String summary) {
			options.setSummary(summary);
			return this;
		}
		public OptionsParser setUsage(String usage) {
			options.setUsage(usage);
			return this;
		}	
		public OptionsParser setOption(String flag, String description) {
			options.setOption(flag, description);
			return this;
		}		
	}
	
	public static class Options {
		private String usage;
		private String summary;
		private String examples;
		private Map<String, Option> optionsMap = new HashMap<String, Option>();
		private Map<String, Option> extraOptionsMap = new HashMap<String, Option>();
		private List<String> args = new ArrayList<String>();
		private boolean useShortFlag = true;
		
		public void setUseShortFlag(boolean useShortFlag) {
			this.useShortFlag = useShortFlag;
		}
		public void setExamples(String examples) {
			this.examples = examples;
		}
		public void setSummary(String summary) {
			this.summary = summary;
		}
		public void setUsage(String usage) {
			this.usage = usage;
		}	
		public void setOption(String flag, String description) {
			String valueFormat = null;
			int valueFormatPos = flag.indexOf("=");
			if (valueFormatPos > 0) {
				valueFormat = flag.substring(valueFormatPos + 1);
				flag = flag.substring(0, valueFormatPos);
			}
			if (optionsMap.get(flag) != null) {
				throw new RuntimeException("Duplicated option " + flag + "=" + optionsMap.get(flag));
			}
			Option opt = new Option(flag, valueFormat, description);
			optionsMap.put(flag, opt);
			
			//If a short flag for this option is not set, then add it as well.
			if (flag.length() > 1 && useShortFlag) {
				String shortFlag = String.valueOf(flag.charAt(0));
				if (extraOptionsMap.get(shortFlag) == null) {
					extraOptionsMap.put(shortFlag, opt);
				}
			}
		}			
		
		@Override
		public String toString() {
			return "Options(\n" +
					"  args=" + args + "\n" +
					"  options=" + optionsMap + "\n" +
					"  extraOptions=" + extraOptionsMap + "\n" +
					")";
		}
			
		public Map<String, Option> getOptionsMap() {
			return optionsMap;
		}
		public Map<String, Option> getExtraOptionsMap() {
			return extraOptionsMap;
		}
		public List<String> getArgs() {
			return args;
		}
		public int getArgsSize() {
			return args.size();
		}
		public String getArg(int idx) {
			return args.get(idx);
		}
		public String getArg(int idx, String def) {
			if (args.size() > idx) {
				return args.get(idx);
			}
			return def;
		}
				
		public void showUsage() {
			println("");
			if (summary != null) {
				println("Summary:");
				println(summary);
				println("");
			}
			if (usage != null) {
				println("Usage:");
				println(usage);
				println("");
			}
			println("Options: ([--flag[=value]])");
			int maxFlagLen = findLongestFlag();
			List<String> keys = new ArrayList<String>(optionsMap.keySet());
			Collections.sort(keys);
			for (String flag : keys) {
				Option opt = optionsMap.get(flag);
				
				if (useShortFlag) {
					String shortFlag = String.valueOf(flag.charAt(0));
					Option shortOpt = extraOptionsMap.get(shortFlag);
					if (shortOpt != null && shortOpt.getFlag().equals(opt.getFlag())) {						
						flag = shortFlag + " or " + flag;
					}
				}
				
				if (opt.getValueFormat() != null) {
					flag += "=" + opt.getValueFormat();
				}
				
				System.out.printf("  %-" + maxFlagLen + "s %s\n", flag, opt.getDescription());
			}
			println("");
			
			if (examples != null) {
				println("Examples:");
				println(examples);
				println("");
			}
		}
		public void showExitUsage() {
			showUsage();
			System.exit(1);
		}
		private int findLongestFlag() {
			int maxLen = 0;
			for (String flag : optionsMap.keySet()) {
				int len = flag.length();
				Option opt = optionsMap.get(flag);
				if (opt.getValueFormat() != null) {
					len += opt.getValueFormat().length() + 1;
				}
				if(len > maxLen) {
					maxLen = len;
				}
			}
			
			if (useShortFlag) {
				maxLen += 5; // len of "X or "
			}
			return maxLen;
		}

		/////////////////////////////////////////////////////////////////////
		// Methods that deals with option value (after parsing)
		/////////////////////////////////////////////////////////////////////
		public Option getOption(String flag) {
			if (flag.startsWith("-")) {  //Remove first dash if any.
				flag = flag.substring(1);
				if (flag.startsWith("-")) {   //Remove second dash if any.
					flag = flag.substring(1);
				}
			}
			Option opt = optionsMap.get(flag);
			if (opt == null && useShortFlag) {
				String shortFlag = String.valueOf(flag.charAt(0));
				opt = extraOptionsMap.get(shortFlag);
				if (opt == null) {
					throw new RuntimeException("There is no such option " + flag);
				}
			}			
			return opt;
		}
		public boolean has(String flag) {
			return getOption(flag).getValue() != null;
		}
		
		public boolean hasNo(String flag) {
			return getOption(flag).getValue() == null;
		}
		public String get(String flag) {
			Option opt = getOption(flag);
			return opt.getValue();
		}
		public int getInt(String flag) {
			return Integer.parseInt(get(flag));
		}
		public double getDouble(String flag) {
			return Double.parseDouble(get(flag));
		}
		public File getFile(String flag) {
			return new File(get(flag));
		}
		
		public String get(String flag, String def) {
			Option opt = getOption(flag);
			String val = opt.getValue();
			if (val == null) {
				return def;
			}
			return val;
		}
		public int getInt(String flag, int def) {
			return Integer.parseInt(get(flag, "" + def));
		}
		public double getDouble(String flag, double def) {
			return Double.parseDouble(get(flag, "" + def));
		}
		public File getFile(String flag, String filename) {
			String name = get(flag, filename);
			if (name == null) {
				return null;
			}
			return new File(name);
		}
	}
}
