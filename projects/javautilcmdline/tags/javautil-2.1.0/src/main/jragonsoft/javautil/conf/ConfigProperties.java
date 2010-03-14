/*
 *	  5/4/2006 Zemian Deng
 * 
 *	  Licensed under the Apache License, Version 2.0 (the "License");
 *	  you may not use this file except in compliance with the License.
 *	  You may obtain a copy of the License at
 * 
 *		  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *	  Unless required by applicable law or agreed to in writing, software
 *	  distributed under the License is distributed on an "AS IS" BASIS,
 *	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	  See the License for the specific language governing permissions and
 *	  limitations under the License.
 *																				 
 */

package jragonsoft.javautil.conf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import jragonsoft.javautil.util.RegexUtils;


/**
 * Exntension of our good old java.util.Properties class that support variable
 * substitution in form of ${ExistingName} format.
 * <p>
 * 
 * ExistingName is any key that is previously defined right up to the variable
 * for expansion. This give your config file Max configurability while still
 * enjoy the flexibility and easy of use of java.lang.Properties object.
 * <p>
 * 
 * Since this class extends java.lang.Properties, all rules of properties file
 * applies from parent class.
 * <p>
 * 
 * Sample file <br>
 * 
 * <pre>
 * 
 * foo = bar
 * num = 2005
 * newfoo = ${foo}
 * newnum = ${num}
 * 
 * </pre>
 * 
 * @author zemian
 * @version $Id: ConfigProperties.java 42 2006-05-10 23:25:49Z zemian $
 * @see java.util.Properties
 */
public class ConfigProperties extends Properties {
	/**
	 *  
	 */
	private static final long serialVersionUID = 5186673024921596544L;

	/** Create instance of empty <code>Properties</code> ready for use. */
	public ConfigProperties() {
	}

	/**
	 * Constructor for the ConfigProperties object
	 * 
	 * @param defaults
	 *            Description of the Parameter
	 */
	public ConfigProperties(Properties defaults) {
		super(defaults);
	}

	/**
	 * Create instance of <code>Properties</code> by loading file content into
	 * the Map.
	 * 
	 * @param file
	 *            <code>Properties</code> file.
	 */
	public ConfigProperties(File file) {
		if (file != null) {
			load(file);
		}
	}

	/**
	 * Insert another Properties Map with a prefix on all names.
	 */
	public void putAll(String prefix, Properties props) {
		Set entries = props.entrySet();
		for (Iterator i = entries.iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			setProperty(prefix + (String) e.getKey(), (String) e.getValue());
		}
	}

	/**
	 * Wrapper to #store(OutputStream out);
	 * 
	 * @param filename
	 *            name of the file.
	 */
	public void store(String filename) {
		try {
			File file = new File(filename);
			//if(!file.exists())
			//	file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			store(new FileOutputStream(filename), "ConfigProperties");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Wrapper to call #load(File file).
	 * 
	 * @param filename
	 *            name of the file.
	 */
	public void load(String filename) {
		load(new File(filename));
	}

	/**
	 * Wrapper to call #load(InputStream in)
	 * 
	 * @param file
	 *            Description of the Parameter
	 */
	public void load(File file) {
		try {
			load(new FileInputStream(file));
		} catch (Exception e) {
			throw new RuntimeException("Failed to load: "
					+ file.getAbsolutePath(), e);
		}
	}

	/**
	 * Loading config file from a <code>InputStream</code> object. Will set
	 * all <code>Properties</code> with at least key and value pair. All key
	 * name are trimed before adding to map.
	 * 
	 * @param in
	 *            Description of the Parameter
	 */
	public void load(InputStream in) {
		try {
			Properties p = new Properties();
			p.load(in);

			putAll(p);
			/*
			 * BufferedReader reader = new BufferedReader(new
			 * InputStreamReader(in)); String line = null; while((line =
			 * reader.readLine()) != null) { line = line.trim();
			 * if(line.startsWith("#")) { continue; } String[] pairs =
			 * line.split("\\s*=\\s*", 2); if(pairs.length < 2) { continue; }
			 * //forgive mistake, just not load it. setProperty(pairs[0].trim(),
			 * pairs[1]); }
			 */
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Just like the {@ linke #findPropertyNames}, but the searchRegex MUST
	 * have at least two matcher groups. The first is used as key that hold
	 * another properties map. The second is used to do a group search within
	 * this instance using the first group as prefix.
	 * 
	 * @param searchRegex
	 *            Description of the Parameter
	 * @return Map of Map.
	 * 
	 * public Map findHashedPropertyNames(String searchRegex) { Pattern pattern =
	 * Pattern.compile(searchRegex); Map hashedMap = new ConfigProperties();
	 * for(Iterator itr = keySet().iterator(); itr.hasNext(); ) { String name =
	 * (String)itr.next(); Matcher matcher = pattern.matcher(name);
	 * if(matcher.find() && matcher.groupCount()>=2) { String key =
	 * matcher.group(1); String groupPrefix = matcher.group(2);
	 * hashedMap.put(key, findPropertyGroup(groupPrefix)); } } return hashedMap; }
	 */

	/**
	 * Find all the names in this Properties instances that match to a regex
	 * search string.
	 * 
	 * @param searchRegex
	 *            Description of the Parameter
	 * @return A set of this instance keys that matched regex
	 */
	public Set findPropertyNames(String searchRegex) {
		Pattern pattern = Pattern.compile(searchRegex);
		HashSet matchedNames = new HashSet();
		for (Iterator itr = keySet().iterator(); itr.hasNext();) {
			String name = (String) itr.next();
			if (pattern.matcher(name).find()) {
				matchedNames.add(name);
			}
		}
		return matchedNames;
	}

	/**
	 * Find sub properties group based on a prefix name that forms a group. For
	 * example, the following will forms a group named "mydata.": <br>
	 * 
	 * <pre>
	 * 
	 *  mydata.name =Zemian Deng
	 *  mydata.title =Developer
	 *  mydata.language =Java
	 *  
	 * </pre>
	 * 
	 * @param prefixGroupName -
	 *            the original property name that contains group's prefix.
	 * @return a new instance of ConfigProperties that contain the result, but
	 *         if found, the group's prefix of all key name are removed!
	 */
	public ConfigProperties findPropertyGroup(String prefixGroupName) {
		ConfigProperties ret = new ConfigProperties();
		Set group = findPropertyNames(prefixGroupName);
		for (Iterator itr = group.iterator(); itr.hasNext();) {
			String name = (String) itr.next();
			String value = getProperty(name);
			ret.setProperty(name.substring(prefixGroupName.length()), value);
		}
		return ret;
	}

	/**
	 * Remove group of names and it's value from the propertie map using regex
	 * search against keys.
	 */
	public void removePropertyNames(String searchRegex) {
		Pattern pattern = Pattern.compile(searchRegex);

		for (Iterator i = entrySet().iterator(); i.hasNext();) {
			Map.Entry e = (Map.Entry) i.next();
			String name = (String) e.getKey();
			if (pattern.matcher(name).find()) {
				i.remove();
			}
		}
	}

	/**
	 * Expand variable with existing props values before adding it to map.
	 * 
	 * @param name
	 *            The new property value
	 * @param value
	 *            The new property value
	 * @return Description of the Return Value
	 */
	public Object setProperty(String name, String value) {
		String expandedValue = RegexUtils.expandSubstitutions(value, this);
		return super.setProperty(name, expandedValue);
	}

	/**
	 * Returns the keys in String array
	 */
	public String[] getKeys() {
		String[] names = new String[size()];
		int i = 0;
		for (Enumeration e = propertyNames(); e.hasMoreElements();) {
			names[i++] = (String) e.nextElement();
		}
		return names;
	}

	/**
	 * Returns the values in String array
	 */
	public String[] getValues() {
		String[] values = new String[size()];
		int i = 0;
		for (Iterator itr = values().iterator(); itr.hasNext();) {
			values[i++] = (String) itr.next();
		}
		return values;
	}

	/**
	 * Retreive value or return a defualt if not found.
	 */
	public String getProperty(String name, String def) {
		if (containsKey(name))
			return getProperty(name);
		else
			return def;
	}

	/**
	 * Gets the intProperty attribute of the ConfigProperties object
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The intProperty value
	 */
	public int getIntProperty(String name) {
		return Integer.parseInt(getProperty(name));
	}

	/**
	 * Gets the longProperty attribute of the ConfigProperties object
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The longProperty value
	 */
	public long getLongProperty(String name) {
		return Long.parseLong(getProperty(name));
	}

	/**
	 * Gets the booleanProperty attribute of the ConfigProperties object
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The booleanProperty value
	 */
	public boolean getBooleanProperty(String name) {
		return Boolean.valueOf(getProperty(name)).booleanValue();
	}

	/**
	 * Gets the doubleProperty attribute of the ConfigProperties object
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The doubleProperty value
	 */
	public double getDoubleProperty(String name) {
		return Double.parseDouble(getProperty(name));
	}
}
