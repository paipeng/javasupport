package deng.javaexamples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXB;

/**
 * 
 * Provide a all-in-one base class that subclass may write command
 * line interface program easier.
 * 
 * A typical subclass can do this:
 * 
 * public class MyApp extends CliApp {
 * 	public String getUsage() {
 * 		return "MyApp [--help | --debug | --input=<file> ] [arguments]";
 * 	}
 * 
 * 	public static void main(String[] args) {
 * 		new MyApp().runMain(args);
 * 	}
 * 
 * 	protected void run() {
 *		if (debug)
 *			System.out.println(this.toString());
 *
 *		for (String arg : args) {
 *			System.out.println("Console argument: " + arg);
 *		}
 *	}
 * }
 * 
 *
 * @author Zemian Deng
 * @version Aug 14, 2009
 */
public abstract class ReflectionOptionParser {
	protected boolean help = false;
	protected boolean debug = false;
	protected File input = null;
	protected String[] args = {};
		
	/////////////////////////////////////////////////////////////////////////
	// Methods for setup typical CLI program calling from static main()
	/////////////////////////////////////////////////////////////////////////	
	public void runMain(String[] args) {
		setArgs(parseOptions(args));
		
		if(debug) {
			dump();
		}		
		
		if(help) {
			System.out.println(getUsage());
			System.exit(0);
		}
		
		run();
	}
	
	public String dump() {
		StringWriter writer = new StringWriter();
		JAXB.marshal(this, writer);
		return writer.toString();
	}
	
	public String getUsage() {
		return "Usage: " +
			this.getClass().getName() + " [options] [arguments]\n\n" +
			this.dump() +
			"\n";
	}
	
	protected abstract void run();
		
	/////////////////////////////////////////////////////////////////////////
	// Methods for parsing options and set member field from option value
	/////////////////////////////////////////////////////////////////////////	
	protected String[] parseOptions(String[] argv) {
		List<String> args = new ArrayList<String>();
		for(int i = 0; i < argv.length; i++) {
			String option = null;
			String arg = argv[i];
			//System.out.println("processing arg: " + arg);
			
			if(arg.equals("--")) { // escape all options parsing.
				for(int j = i; j < argv.length; j++)
					args.add(argv[j]);
				break; // break out of all arguments parsing!
			}else if(arg.startsWith("--")) {
				option = arg.substring(2);
			} else {
				args.add(arg);
				continue;
			}
			
			//Process the option value.
			String value = "true";
			if(option.indexOf("=") > 0) {
				String[] words = option.split("=");
				option = words[0];
				value = words[1];
			}
			//transform simple name into setter method name.
			String methodName = "set" + 
				String.valueOf(option.charAt(0)).toUpperCase() +
				option.substring(1);
			try { 
				Method method = findSetMethod(methodName);
				Class<?> paramType = method.getParameterTypes()[0];
				if (paramType.isAssignableFrom(String.class)) {
					method.invoke(this, value);					
				} else if (paramType.getName().equals("int") || 
						paramType.isAssignableFrom(Integer.class)) {
					method.invoke(this, Integer.parseInt(value));
				} else if (paramType.getName().equals("boolean") || 
						paramType.isAssignableFrom(Boolean.class)) {
					method.invoke(this, Boolean.valueOf(value));
				} else if (paramType.getName().equals("double") || 
						paramType.isAssignableFrom(Double.class)) {
					method.invoke(this, Double.parseDouble(value));
				} else if (paramType.isAssignableFrom(File.class)) {
					method.invoke(this, new File(value));
				} else {
					handleUnknownOption(option, method, value);
				}
			} catch (Exception e) {
				throw new RuntimeException("Failed to set option " + arg, e);
			}
		}
		return args.toArray(new String[args.size()]);
	}
		
	private Method findSetMethod(String methodName) {
		//System.out.println("Searching for method: " + methodName);
		Class<?> cls = this.getClass();
		Method[] methods = cls.getMethods();
		for (int i = 0; i < methods.length; i++) {
			Method m = methods[i];
			if(m.getName().equals(methodName) && m.getParameterTypes().length == 1) {
				return m;
			}
		}
		throw new RuntimeException("Unable to find method, " + methodName + ", to set option.");
	}
		
	protected void handleUnknownOption(String option, Method method, String value) {
		String typeName = method.getParameterTypes()[0].getName();	
		throw new RuntimeException("Invalid method parameter types. Requireed: " + typeName);
	}
		
	/////////////////////////////////////////////////////////////////////////
	// Methods for Setters and Getters
	/////////////////////////////////////////////////////////////////////////
	public void setHelp(boolean help) {
		this.help = help;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isHelp() {
		return help;
	}

	public boolean isDebug() {
		return debug;
	}
	public String[] getArgs() {
		return args;
	}
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public void setInput(File input) {
		this.input = input;
	}
	
	public File getInput() {
		return input;
	}

	/////////////////////////////////////////////////////////////////////////
	// Methods for text file processing
	/////////////////////////////////////////////////////////////////////////	
	public static interface LineAction {
		public void onLine(String line);
	}
	public static void eachLine(File file, LineAction action) {
		try {
			eachLine(new FileInputStream(file), action);
		} catch (FileNotFoundException e) {
			throw new RuntimeException("Failed to read from file " + file, e);
		}
	}
	public static void eachLine(InputStream ins, LineAction action) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(ins));
			String line = null;
			while((line = reader.readLine()) != null) {
				action.onLine(line);
			}
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
	
	public static interface PrinterWriterAction {
		public void onPrintWriter(PrintWriter writer);
	}	
	public static void withPrintWriter(File file, PrinterWriterAction action) {
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			action.onPrintWriter(writer);
			writer.flush();
		} catch(Exception e) {
			throw new RuntimeException("Failed to write to file " + file, e);
		} finally {
			if(writer != null)
				writer.close();
		}
	}
	
	public static InputStream getInputFileOrStandardInputStream(File file) {
		InputStream ins = null;
		if(file != null) {
			try {
				ins = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			}
		} else {
			ins = System.in;
		}
		return ins;
	}
}
