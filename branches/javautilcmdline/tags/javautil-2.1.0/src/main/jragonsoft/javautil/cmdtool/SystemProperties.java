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

package jragonsoft.javautil.cmdtool;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import jragonsoft.javautil.support.GetOpt;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SystemProperties.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SystemProperties {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  SystemProperties [options]"
				+ "\n  Print JVM's system properties in sorted order."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ systemproperties"
				+ "\n  $ systemproperties | grep sep"
				+ "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SystemProperties.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SystemProperties class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}

		TreeMap props = new TreeMap(System.getProperties());
		for (Iterator itr = props.entrySet().iterator(); itr.hasNext();) {
			Map.Entry item = (Map.Entry) itr.next();
			System.out.println((String) item.getKey() + "="
					+ (String) item.getValue());
		}
	}
}
