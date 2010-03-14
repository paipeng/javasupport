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
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.util.RegexUtils;
import jragonsoft.javautil.util.StringUtils;


/**
 * A very simple implemenation of printf function. This version only supports
 * three conversion: %s, %d, and %f. For more sophiscated and complete
 * functinality, you should use Java 1.5's printf function.
 * <p>
 * 
 * This class provide a very simple convient method that cascade parameters as
 * argument for printf simulation. The ideas is from one of the existing
 * implementation of printf out there. See reference and usage below.
 * <p>
 * 
 * Reference: http://sharkysoft.com/software/java/lava3/printf
 * <p>
 * 
 * Format specifieer syntax:
 * 
 * <pre>
 * 
 * %[flags][width][.precision][input_size]conversion_type
 * 
 * </pre>
 * 
 * <p>
 * 
 * Usage:
 * 
 * <pre>
 * 
 * Printf.out(&quot;My name is %s, and I am %d years old.&quot;, Printf.add(&quot;Zemian&quot;).add(28));
 * String[] names = {&quot;Mighty Knight&quot;, &quot;Rooky&quot;, &quot;Cool Bishop&quot;};
 * double[] scores = {99.99, 100.00, 100.00}
 * for(int i =0; i &lt; names.length; i++){
 *     Printf.out(&quot;%-32s %3.1f&quot;, Printf.add(names[i]).add(scores[i]);
 * }
 * 
 * </pre>
 * 
 * @author zemian
 * @version $Id: Printf.java 26 2006-05-04 23:46:14Z zdeng $
 */
public class Printf {
	private final static boolean ISDEBUG = false;

	private final static String FORMAT_PREFIX = "%";

	private final static String FORMAT_PREFIX_RE = "\\%\\%";

	private final static String FORMAT_SPECIFIER_RE = "([^\\%])(\\%([+-0]{0,1})(\\d*)(\\.{0,1}\\d*)(\\w{0,1})(\\w{1}))"; //(?<=^\\s*|[^\\%])

	private static Pattern pattern = Pattern.compile(FORMAT_SPECIFIER_RE,
			Pattern.MULTILINE);

	private static Pattern patternPrefix = Pattern.compile(FORMAT_PREFIX_RE,
			Pattern.MULTILINE);

	private static HashSet supportedTypes = new HashSet();

	static {
		supportedTypes.add("s");
		supportedTypes.add("f");
		supportedTypes.add("d");
	}

	private static void debug(String s) {
		if (ISDEBUG) {
			System.out.println("[DEBUG] " + s);
		}
	}

	public static void out(String format, Param param) {
		System.out.print(format(format, param));
	}

	public static void print(String format, Param param) {
		System.out.print(format(format, param));
	}

	public static void println(String format, Param param) {
		System.out.println(format(format, param));
	}

	public static String format(String format) {
		return format(format, new Param());
	}

	public static String format(String format, String firstArg) {
		return format(format, add(firstArg));
	}

	public static String format(String format, int firstArg) {
		return format(format, add(firstArg));
	}

	public static String format(String format, double firstArg) {
		return format(format, add(firstArg));
	}

	/**
	 * A simple printf equavelent implementation function. For now, it only
	 * supports "%s", "%f" and "%d"
	 * 
	 * @param format
	 *            Description of the Parameter
	 * @param param
	 *            Description of the Parameter
	 */
	public static String format(String format, Param param) {

		format = "_" + format;

		Matcher matcher = pattern.matcher(format);
		StringBuffer sb = new StringBuffer();
		debug("format=" + format);
		for (int paramIdx = 0, maxParam = param.getParamCount(); paramIdx < maxParam; paramIdx++) {
			if (!matcher.find()) {
				throw new RuntimeException("Wrong argument count at #"
						+ paramIdx);
			}
			debug("FoundMatched");
			int count = 1;
			String firstChar = matcher.group(count++);
			//Group#1:
			String replace = matcher.group(count++);
			//Group#2:
			String flag = matcher.group(count++);
			//Group#3:
			String width = matcher.group(count++);
			//Group#4:
			String precision = matcher.group(count++);
			//Group#5:
			String inputSize = matcher.group(count++);
			//Group#6:
			String convType = matcher.group(count++);
			//Group#7:

			debug("firstChar=" + firstChar + "replace=" + replace + " flag="
					+ flag + " width=" + width + " precision=" + precision
					+ " inputSize=" + inputSize + " convType=" + convType);

			if (!isSupportedType(convType)) {
				throw new RuntimeException("Conversion type \"" + convType
						+ "\" is not supported");
			}

			try {
				String arg = param.get(paramIdx);

				if (convType.equals("f") && !precision.equals("")) {
					int len = Integer.parseInt(precision.substring(1));
					arg = new Double(arg).toString();
					String[] parts = arg.split("\\.");
					if (parts.length < 2) {
						throw new RuntimeException("Wrong float argument at #"
								+ paramIdx);
					}
					if (parts[1].length() >= len) {
						parts[1] = parts[1].substring(0, len);
					} else {
						parts[1] = StringUtils.padBackChar(parts[1], len, '0');
					}

					arg = parts[0] + "." + parts[1];
				}

				if (convType.equals("d")) {
					arg = "" + (long) new Double(arg).doubleValue();
				}

				if (!width.equals("")) {
					int width_ = Integer.parseInt(width);
					if (flag.equals("-") && arg.length() < width_) {
						arg = StringUtils.padBackChar(arg, width_, ' ');
					} else if (flag.equals("0")) {
						if (convType.equals("f")) {
							String[] parts = arg.split("\\.");
							if (parts[0].length() < width_) {
								arg = StringUtils.padFrontChar(parts[0],
										width_, '0')
										+ "." + parts[1];
							}
						} else if (convType.equals("d")
								&& arg.length() < width_) {
							arg = StringUtils.padFrontChar(arg, width_, '0');
						}
					} else if ((flag.equals("") || flag.equals("+"))
							&& arg.length() < width_) {
						arg = StringUtils.padFrontChar(arg, width_, ' ');
					}
				}
				arg = RegexUtils.escapeRegex(firstChar + arg);
				matcher.appendReplacement(sb, arg);
			} catch (IndexOutOfBoundsException e) {
				throw new RuntimeException("Wrong argument count at #"
						+ paramIdx, e);
			} catch (NumberFormatException e) {
				throw new RuntimeException("Wrong syntax format specifier at #"
						+ paramIdx, e);
			}
		}

		matcher.appendTail(sb);
		format = sb.toString();
		format = StringUtils.trimFront(format, "_");

		//replace all escaped %
		matcher = patternPrefix.matcher(format);
		if (matcher.find()) {
			format = matcher.replaceAll(FORMAT_PREFIX);
		}

		return format;
	}

	/**
	 * Gets the supportedType attribute of the Printf class
	 * 
	 * @param type
	 *            Description of the Parameter
	 * @return The supportedType value
	 */
	private static boolean isSupportedType(String type) {
		return supportedTypes.contains(type);
	}

	/**
	 * One of Convientient wrapper method to create a new instance of
	 * jragonsoft.javautil.util.Printf.Param object and return it's ref for cascading
	 * use.
	 * 
	 * @param arg
	 *            an argument to format string.
	 * @return the newly created jragonsoft.javautil.util.Printf.Param instance.
	 */
	public static Param add(String arg) {
		return new Param(arg);
	}

	/**
	 * One of Convientient wrapper method to create a new instance of
	 * jragonsoft.javautil.util.Printf.Param object and return it's ref for cascading
	 * use.
	 * 
	 * @param arg
	 *            an argument to format string.
	 * @return the newly created jragonsoft.javautil.util.Printf.Param instance.
	 */
	public static Param add(int arg) {
		return new Param(arg);
	}

	/**
	 * One of Convientient wrapper method to create a new instance of
	 * jragonsoft.javautil.util.Printf.Param object and return it's ref for cascading
	 * use.
	 * 
	 * @param arg
	 *            an argument to format string.
	 * @return the newly created jragonsoft.javautil.util.Printf.Param instance.
	 */
	public static Param add(double arg) {
		return new Param(arg);
	}

	/**
	 * Inner STATIC class that serve was printf's format string's parameters.
	 * 
	 * @author zemian
	 * @version $Id: Printf.java 26 2006-05-04 23:46:14Z zdeng $
	 */
	public static class Param {
		/** Description of the Field */
		private ArrayList params = new ArrayList();

		/** Constructor for the Param object */
		public Param() {
		}

		/**
		 * Constructor for the Param object
		 * 
		 * @param arg
		 *            Description of the Parameter
		 */
		public Param(String arg) {
			add(arg);
		}

		/**
		 * Constructor for the Param object
		 * 
		 * @param arg
		 *            Description of the Parameter
		 */
		public Param(int arg) {
			add(arg);
		}

		/**
		 * Constructor for the Param object
		 * 
		 * @param arg
		 *            Description of the Parameter
		 */
		public Param(double arg) {
			add(arg);
		}

		/**
		 * Description of the Method
		 * 
		 * @param arg
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		public Param add(String arg) {
			params.add(arg);
			return this;
		}

		/**
		 * Description of the Method
		 * 
		 * @param arg
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		public Param add(int arg) {
			params.add(new Integer(arg));
			return this;
		}

		/**
		 * Description of the Method
		 * 
		 * @param arg
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		public Param add(double arg) {
			params.add(new Double(arg));
			return this;
		}

		/**
		 * Description of the Method
		 * 
		 * @param index
		 *            Description of the Parameter
		 * @return Description of the Return Value
		 */
		public String get(int index) {
			return params.get(index).toString();
		}

		/**
		 * Gets the paramCount attribute of the Param object
		 * 
		 * @return The paramCount value
		 */
		public int getParamCount() {
			return params.size();
		}

		/**
		 * Description of the Method
		 * 
		 * @return Description of the Return Value
		 */
		public String toString() {
			return params.toString();
		}
	}
}