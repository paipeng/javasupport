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

package jragonsoft.javautil.support;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import jragonsoft.javautil.util.ArrayUtils;


/**
 * Parse command line with SHORT style option arguments. The instance will allow
 * user to retrieve and test option values easily. This class also provide
 * actual arguments array access after the options is parsed.
 * <p>
 * 
 * The SHORT style option arguments are commonly used in many Unix style
 * program. The basic syntax is to prefix one dash in front of a word to make it
 * an option. This class implementation ALLOW option's word to be any length.
 * Option's argument is also allowed right after the option WORD. No whitespace
 * is allowed per single option including the option's argument. The order of
 * option and actual argments can be mixed.
 * <p>
 * 
 * For example:
 * 
 * <pre>
 * 
 *   $ myprogram -d one two three     # d option is ON or exists, while only one two three are three actual arguments.
 *   $ myprogram -f/tmp/one.txt       # f option with an argument &quot;/tmp/one.txt&quot;
 *   $ myprogram -version             # version option is ON or exists
 *   $ myprogram -max2000             # max option with an argument &quot;2000&quot;
 *   
 * </pre>
 * 
 * To use this class:
 * 
 * <pre>
 * public static void main(String[] args) {
 * 	GetOpt opt = new GetOpt(args);
 * 	if (opt.isOpt(&quot;d&quot;))
 * 		System.out.println(&quot;[DEBUG] debug is ON&quot;);
 * 	if (opt.isOpt(&quot;version&quot;))
 * 		System.out.println(&quot;[DEBUG] version is ON&quot;);
 * 	String filename = opt.getOpt(&quot;f&quot;, &quot;tmp.text&quot;); //get option argument with def set if not exits.
 * 	int max = opt.getIntOpt(&quot;max&quot;, 1000); //get option argument with def set if not exits.
 * 
 * 	//Access actual arguments after all the options are parsed.
 * 	for (int i = 0; i &lt; opt.getArgsCount(); i++) {
 * 		String arg = opt.getArg(i);
 * 		System.out.println(&quot;[DEBUG] &quot; + arg);
 * 	}
 * }
 * </pre>
 * 
 * <p>
 * 
 * The option may be esacape using a slash in front like "\-".
 * 
 * The parameter value to the option are single string value, and can't have
 * space in it.
 * 
 * @author zemian
 * @version $Id: GetOpt.java 26 2006-05-04 23:46:14Z zdeng $
 */
public class GetOpt {
	/** Description of the Field */
	protected ArrayList args = new ArrayList();

	/** Description of the Field */
	protected Properties opts = new Properties();

	/** Description of the Field */
	protected boolean isKeepOptionFlag = false;

	/** Constructor for the GetOpt object */
	public GetOpt() {
	}

	/**
	 * Constructor for the GetOpt object
	 * 
	 * @param cmdargs
	 *            Description of the Parameter
	 */
	public GetOpt(String[] cmdargs) {
		parse(cmdargs);
	}

	/**
	 * If set to true, all option name are inserted in map along with the
	 * dash(s).
	 * 
	 * @param flag
	 *            The new keepOptionFlat value
	 */
	public void setKeepOptionFlag(boolean flag) {
		isKeepOptionFlag = flag;
	}

	/**
	 * Parsing a short option style of argument list.
	 * 
	 * @param cmdargs
	 *            Description of the Parameter
	 */
	public void parse(String[] cmdargs) {
		for (int i = 0, maxcmdargs = cmdargs.length; i < maxcmdargs; i++) {
			String arg = cmdargs[i];

			//Allow escape of option dash
			if (arg.startsWith("\\-")) {
				args.add(arg.substring(1));
				continue;
				//If arg is not start with dash, treated as reg arg.
			} else if (!arg.startsWith("-")) {
				args.add(arg);
				continue;
			}

			String optName = isKeepOptionFlag ? arg.substring(0, 2) : arg
					.substring(1, 2);
			String optValue = arg.substring(2);
			if (optValue.equals("") || optValue == null) {
				opts.setProperty(optName, "true");
			} else {
				opts.setProperty(optName, optValue.trim());
			}
		}
	}

	/**
	 * Gets the args attribute of the GetOpt object
	 * 
	 * @return The args value
	 */
	public String[] getArgs() {
		return ArrayUtils.toStringArray(args);
	}

	/**
	 * Gets the argsCount attribute of the GetOpt object
	 * 
	 * @return The argsCount value
	 */
	public int getArgsCount() {
		return args.size();
	}

	/**
	 * Gets the arg attribute of the GetOpt object
	 * 
	 * @param idx
	 *            Description of the Parameter
	 * @return The arg value
	 */
	public String getArg(int idx) {
		return (String) args.get(idx);
	}

	/**
	 * Gets the intArg attribute of the GetOpt object
	 * 
	 * @param idx
	 *            Description of the Parameter
	 * @return The intArg value
	 */
	public int getIntArg(int idx) {
		return Integer.parseInt((String) args.get(idx));
	}

	/**
	 * Gets the doubleArg attribute of the GetOpt object
	 * 
	 * @param idx
	 *            Description of the Parameter
	 * @return The doubleArg value
	 */
	public double getDoubleArg(int idx) {
		return Double.parseDouble((String) args.get(idx));
	}

	/**
	 * Determine wheter an option is given or not.
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @return The opt value
	 */
	public boolean isOpt(String opt) {
		return opts.containsKey(opt);
	}

	/**
	 * Determine wheter an option is given or not.
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @return The opt value
	 */
	public boolean isNotOpt(String opt) {
		return !opts.containsKey(opt);
	}

	/**
	 * Gets the opt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The opt value
	 */
	public String getOpt(String opt, String def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return opts.getProperty(opt);
	}

	/**
	 * Gets the intOpt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The intOpt value
	 */
	public int getIntOpt(String opt, int def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return Integer.parseInt(opts.getProperty(opt));
	}

	/**
	 * Gets the longOpt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The longOpt value
	 */
	public long getLongOpt(String opt, long def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return Long.parseLong(opts.getProperty(opt));
	}

	/**
	 * Gets the floatOpt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The floatOpt value
	 */
	public float getFloatOpt(String opt, float def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return Float.parseFloat(opts.getProperty(opt));
	}

	/**
	 * Gets the doubleOpt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The doubleOpt value
	 */
	public double getDoubleOpt(String opt, double def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return Double.parseDouble(opts.getProperty(opt));
	}

	/**
	 * Gets the booleanOpt attribute of the GetOpt object
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The booleanOpt value
	 */
	public boolean getBooleanOpt(String opt, boolean def) {
		if (!opts.containsKey(opt)) {
			return def;
		}
		return Boolean.valueOf(opts.getProperty(opt)).booleanValue();
	}

	/**
	 * Gets the opt attribute of the GetOpt object
	 * 
	 * @return The opt value
	 */
	public Properties getOpt() {
		return opts;
	}

	/**
	 * Return a string represation of this opt object after parsed. Good for
	 * debugging.
	 * 
	 * @return Description of the Return Value
	 */
	public String toString() {
		StringBuffer sb = new StringBuffer("=== " + super.toString() + "\n");
		sb.append("[options]\n");
		for (Iterator itr = opts.keySet().iterator(); itr.hasNext();) {
			String key = (String) itr.next();
			sb.append("  " + key + ": " + opts.getProperty(key) + "\n");
		}
		sb.append("[Arguments]\n");
		for (int i = 0, maxIdx = args.size(); i < maxIdx; i++) {
			sb.append("  " + (i) + ": " + (String) args.get(i) + "\n");
		}
		sb.append("=== " + super.toString() + "\n");

		return sb.toString();
	}
}