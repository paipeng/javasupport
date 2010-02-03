package deng.pojo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

//import org.apache.log4j.Logger;

/**
 * A smart and flexible class to store and load keys and values, or Properties.
 * 
 * This class is similar to java.lang.Properties, but with these extra features:
 *   - Expandable variables that can substitute existing properties values.
 *   - Load any properties from URL path, including a "classpath:" protocol.
 *   - Convenient constructor to load a properties file.
 *   - Convenient getter methods to convert properties values into specific types.
 * 
 * Use {@link #setString(String, String)} and {@link #getString(String)} to manage
 * property values.
 * 
 * The expandable variable format is ${key} or ${key:defaultValue}. You may use '\'
 * to escape the dollar sign. Variable key is used to search existing properties values,
 * and if not found it will default to search in System Properties, and then if not found
 * it will throw RuntimeException (unless you provide a default value).
 * 
 * Note that you may not have variable inside another variable key. All key are simple
 * string name as lookup key.
 * 
 * Usage example:

# Content of a properties file: my.properties
mydir = ${user.home}/mydir
myscript = ${script.home:/home/zemian}/myscript.groovy
mylist = one, two, three
mynum = 123
myrate = 88.3
mymap = one=>good, two=>great, three=>super great

#Java usage
SmartProperties props = new SmartProperties("my.properties");
String mydir = props.getString("mydir");
File myscript = props.getFile("myscript");
int mynum = props.getString("mynum");
double myrate = props.getString("myrate");
List<String> mylist = props.getList("mylist", ", ");
Map<String, String> mymap = props.getMap("mymap", ", ", "=>");

#Adding your own key and values
SmartProperties props = new SmartProperties();
props.setString("test1", "bar");
props.setString("test2", "foo ${test1}");
props.setString("test3", "foo ${testOptional:bar}");

#Loading extra files
SmartProperties props = new SmartProperties();
props.loadFile("my2.properties");
props.loadURL("http://myhost.com/myapp/config.properties");
props.loadURL("classpath:com/myhost/myapp/config.properties");

#Storing props into file
props.store("myconfig.properties");

#Storing expanded version of file.
FileWriter writer = new FileWriter("myExpandedConfig.properties");
boolean expandVariables = true;
props.store(writer, expandVariables);

#Working with existing java.lang.Properties
Properties javaProps = new Properties();
javaPros.load(new FileReader("my.properties"));
...
SmartProperties props = new SmartProperties(javaProps);
Set<String> myvars = props.getKeys("my.*");
List<String> myvals = props.getString(myvars);
boolean expandVariable = true;
Properties javaProps2 = props.toProperties(expandVariables);

 * 
 * @author dengz1
 *
 */
public class SmartProperties {
	// Commented out logger to minimize dependency for using this class.
	//private static Logger logger = Logger.getLogger(SmartProperties.class);
		
	private Properties props = new Properties();
	private Properties systemProps = System.getProperties();
	private boolean enableSystemPropsLookup = true;
	
	/**
	 * Construct an instance.
	 */
	public SmartProperties() {
		
	}
	
	/**
	 * Construct an instance and load the properties file path.
	 * @param path
	 */
	public SmartProperties(String path) {
		loadFile(path);
	}
	
	/**
	 * Construct an instance and load from java.lang.Properties obj.
	 * 
	 * @param path
	 */
	public SmartProperties(Properties otherProps) {
		loadProps(otherProps);
	}
	
	/**
	 * Load java.lang.Properties into this class.
	 *  
	 * @param otherProps
	 */
	public void loadProps(Properties otherProps) {
		props.putAll(otherProps);
	}

	/**
	 * Load a properties file from a path name.
	 * 
	 * @param path
	 */
	public void loadFile(String path) {
		String expandedPath = expandVariables(path);
		try {
			loadURL(new File(expandedPath).toURI().toURL());
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This method will detect "classpath:" protocol before converting to an URL object.
	 * 
	 * If "classpath:" is found, it will create an URL that points to the classpath
	 * resource, which will be loaded using the current thread's ClassLoader. This is 
	 * done instead of writing URL custom handler for easy workaround.
	 * 
	 * Also, the urlName may contain expandable variables, which will be processed first.
	 * 
	 * @param urlName
	 */
	public void loadURL(String urlName) {
		//logger.debug("Loading urlName " + urlName);
		urlName = expandVariables(urlName);		
		URL url = null;
		if (urlName.startsWith("classpath:")) {
			String resName = urlName.substring(10);
			//logger.debug("Loading URL from classpath resource: " + resName);
			url = Thread.currentThread().getContextClassLoader().getResource(resName);
			if (url == null) {
				throw new RuntimeException("Unable to find classpath resource: " + resName);
			}
		} else {
			try {
				url = new URL(urlName);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		
		loadURL(url);
	}
	
	/** 
	 * Load properties file from a URL. 
	 *
	 * @param urlName
	 */
	
	public void loadURL(URL url) {
		//logger.debug("Loading url " + url);
		InputStream ins = null;
		Properties p = new Properties();
		try {
			ins = url.openStream();
			p.load(ins);
			props.putAll(p);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				ins.close();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	/**
	 * Set flag to use {@link System#getProperties()} during {@link #expandVariables(String)} or not.
	 * 
	 * @param useSystemProperties
	 */
	public void setEnableSystemPropsLookup(boolean useSystemProperties) {
		this.enableSystemPropsLookup = useSystemProperties;
	}
	
	/**
	 * Expand variable substitution on the input string. The variable name is used as
	 * the key to search in the loaded properties for values. If no variable is found
	 * in the string, the original string is returned.
	 * 
	 * This method will throw RuntimeException if key is not found unless a default
	 * value is given.
	 * 
	 * @param input the string that contain any variables for substitution.
	 * @return expanded string value all variables substituted by matched properties values.
	 */
	protected String expandVariables(final String input) {
		// Case1: null input
		//System.out.println("input: " + input + "\nlenght=" + input.length());
		if (input == null) {
			return null;
		}
		
		StringBuilder expandedValue = new StringBuilder("");
		int curPos = 0;
		int lastFoundPos = 0;
		while ((curPos = input.indexOf("${", curPos)) >=0) {
			// curPos is the found position
			//System.out.println("curPos = " + curPos);
			
			// Case2: Contains escape char before ${
			//        Will skip to next while loop.
			if (curPos > 0 && input.charAt(curPos - 1) == '\\') {				
				expandedValue.append(input.substring(lastFoundPos, curPos - 1));
				expandedValue.append("${");				
				curPos += 2;
				lastFoundPos = curPos;
				continue;
			}
			int endPos = input.indexOf("}", curPos);
			//System.out.println("endPos = " + endPos);
			
			// Case3: If we find a starting ${ but no ending }, then it's not a valid 
			//        variable. We end end the search here.
			if (endPos == -1) {
				break; // Let's get out of while loop.
			}			
			
			// Case4: Variable is found!
			String varName = input.substring(curPos + 2, endPos);
			//logger.debug("Expanding variable " + varName);
			int defValPos = varName.indexOf(":");

			String defVal = null;			
			
			// Case4A: The variable contains default value.
			if (defValPos > 0) {
				defVal = varName.substring(defValPos + 1);
				varName = varName.substring(0, defValPos);
			}			
			
			// Case4B: Error out if the variable is not found and no default is provided
			if (!props.containsKey(varName)) {
				if (enableSystemPropsLookup && !systemProps.containsKey(varName)) {
					if (defVal == null) {
						throw new RuntimeException("Unable to find expandable variable named \"" + varName + "\".");
					}
				}
			}	
			
			// HappyCase: Searching for substitution value.
			// Not that getString will recurse if value itself contain other variables!
			String varVal = getString(varName, defVal);
			//logger.info("Expanding variable " + varName + " to " + varVal);
			//System.out.println("Expanding variable " + varName + " to " + varVal);	

			expandedValue.append(input.substring(lastFoundPos, curPos));
			expandedValue.append(varVal);

			curPos = endPos + 1;
			lastFoundPos = curPos;
			//System.out.println("next curPos = " + curPos);
			//System.out.println("input[nextPos] = " + (curPos < input.length() ? input.charAt(curPos) : "<<END"));
		}
		expandedValue.append(input.substring(lastFoundPos));
		
		return expandedValue.toString();
	}
		
	/**
	 * Write all props keys and values into a writer object.
	 * 
	 * @param writer - output writer
	 * @param expandVariables - flag to indicate for variable expansion or not.
	 */
	public void store(Writer writer, boolean expandVariables) {
		try {
			toProperties(expandVariables).store(writer, "SmartProperties " + new Date());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Write all props keys and values into a file with variables unexpanded.
	 * 
	 * @param file - output file
	 */
	public void store(String file) {
		try {
			store(new FileWriter(file), false);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Converting this properties object into java.lang.Properties.
	 * 
	 * @param expandVariables a flag to expand the variable or not.
	 * @return a new instance of java.lang.Properties.
	 */
	public Properties toProperties(boolean expandVariables) {
		Properties ret = new Properties();
		if (expandVariables) {
			Set<String> names = getKeys();
			for (String name : names) {
				ret.put(name, getString(name));
			}
		} else {
			ret.putAll(props);
		}		
		return ret;
	}
	
	/** Return set of all keys for this props object. */
	public Set<String> getKeys() {
		return props.stringPropertyNames();
	}
	
	/** Return set of keys that match given regex pattern. */
	public Set<String> getKeys(String pattern) {
		Set<String> names = props.stringPropertyNames();
		Set<String> ret = new HashSet<String>();
		for (String name : names) {
			if (name.matches(pattern)) {
				ret.add(name);
			}
		}
		return ret;
	}
	
	/**
	 * Add new key value pair into this props object.
	 * 
	 * @param key
	 * @param value
	 */
	public void setString(String key, String value) {
		props.setProperty(key, value);
	}
	
	/** 
	 * Get value by key and expand variables if found.
	 * 
	 * @param key
	 * @return
	 */
	public String getString(String key) {
		String val = props.getProperty(key);
		if (val == null && enableSystemPropsLookup) {
			val = systemProps.getProperty(key);
		}
		return expandVariables(val);
	}
	/**
	 * Get value by key and expand variables if found. If key is not found, use def.
	 * 
	 * @param key
	 * @param def
	 * @return
	 */
	public String getString(String key, String def) {
		String val = getString(key);
		if (val == null) {
			val = def;
		}
		return expandVariables(val);
	}
	
	/**
	 * Get all expanded values that are in set of keys.
	 * 
	 * @param pattern
	 * @return
	 */
	public List<String> getString(Set<String> keys) {
		List<String> ret = new ArrayList<String>();
		for (String key : keys) {
			ret.add(getString(key));
		}
		return ret;
	}

	// =======================================================
	// Convenient methods to retrieve different type of values
	// =======================================================
	public int getInt(String key) {
		return Integer.parseInt(getString(key));
	}
	public int getInt(String key, int def) {
		return Integer.parseInt(getString(key, "" + def));
	}

	public long getLong(String key) {
		return Long.parseLong(getString(key));
	}
	public long getLong(String key, long def) {
		return Long.parseLong(getString(key, "" + def));
	}
	
	public double getDouble(String key) {
		return Double.parseDouble(getString(key));
	}
	public double getDouble(String key, String def) {
		return Double.parseDouble(getString(key, def));
	}
	
	public File getFile(String key) {
		return new File(getString(key));
	}
	public File getFile(String key, String def) {
		return new File(getString(key, def));
	}
	
	public List<String> getList(String key, String listSep) {
		return Arrays.asList(getString(key).split(listSep));
	}
	
	public Map<String, String> getMap(String key, String listSep, String pairSep) {
		List<String> pairs = getList(key, listSep);
		Map<String, String> map = new HashMap<String, String>();
		for (String pair : pairs) {
			String[] splits = pair.split(pairSep);
			map.put(splits[0], splits[1]);
		}
		return map;
	}
}
