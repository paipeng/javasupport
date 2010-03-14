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

/**
 * This extends GetOpt where now also provide implementation for parsing LONG
 * style option arguments. Now short AND long style may be mixed!
 * <p>
 * 
 * With LONG style, option's arguments are provided with equal signs. Again No
 * whitespace is allowed per single option including the option's argument. The
 * order of option and actual argments can be mixed.
 * <p>
 * 
 * Only parsing implementation is added, while the usage is still the same as
 * the <code>GetOpt</code> class.
 * <p>
 * 
 * For example:
 * 
 * <pre>
 * 
 *   $ myprogram --d one two three     # d option is ON or exists, while only one two three are three actual arguments.
 *   $ myprogram --f=/tmp/one.txt       # f option with an argument &quot;/tmp/one.txt&quot;
 *   $ myprogram --version             # version option is ON or exists
 *   $ myprogram --max=2000             # max option with an argument &quot;2000&quot;
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
 * @author zemian
 * @version $Id: GetLongOpt.java 26 2006-05-04 23:46:14Z zdeng $
 * @see jragonsoft.javautil.support.GetOpt
 */
public class GetLongOpt extends GetOpt {

	/** Constructor for the GetLongOpt object */
	public GetLongOpt() {
	}

	/**
	 * Constructor for the GetLongOpt object
	 * 
	 * @param args
	 *            Description of the Parameter
	 */
	public GetLongOpt(String[] args) {
		parse(args);
	}

	/**
	 * Parsing a short and longf option style of argument list.
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
				//parsing long option
			} else if (arg.startsWith("--")) {
				String optStr = arg.substring(2);
				String[] optAry = optStr.split("=");
				String optName = isKeepOptionFlag ? "--" + optAry[0].trim()
						: optAry[0].trim();
				if (optAry.length == 2) {
					opts.setProperty(optName, optAry[1].trim());
				} else {
					opts.setProperty(optName, "true");
				}
				continue;
				//yes, we still allow short option as well!
			} else if (arg.startsWith("-")) {
				String optStr = arg.substring(1);
				String[] optAry = optStr.split("=");
				String optName = isKeepOptionFlag ? "-" + optAry[0].trim()
						: optAry[0].trim();
				if (optAry.length == 2) {
					opts.setProperty(optName, optAry[1].trim());
				} else {
					opts.setProperty(optName, "true");
				}
				continue;
			}
			args.add(arg);
		}
	}
}