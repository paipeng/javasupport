/*
 *    Copyright 2005 Zemian Deng
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *                                                                               
 */

package jragonsoft.javautil.cmdtool;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Just a dummy class. Doesn't do anything useful.
 * 
 * 
 * @author zemian
 * @version $Id: Main.java 4 2006-03-16 15:27:19Z zemian $
 */
public class Main {
	/**
	 * Gets the releaseName attribute of the Main class
	 * 
	 * @return The releaseName value
	 */
	public static String getReleaseName() {
		return "$Id: Main.java 4 2006-03-16 15:27:19Z zemian $";
	}

	/**
	 * Gets the version attribute of the Main class
	 * 
	 * @return The version value
	 */
	public static String getVersion() {
		return "$Id: Main.java 4 2006-03-16 15:27:19Z zemian $";
	}

	/**
	 * The main program for the Main class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		Properties opt = new Properties();
		args = parseOpt(args, opt);

		if (opt.containsKey("h") || opt.containsKey("help") || args.length != 0) {
			printExitHelp();
		}

		System.out.println(getVersion());
	}

	/** Description of the Method */
	public static void printExitHelp() {
		System.out.println("USAGE: Main [options]");
		System.out
				.println("javautil is a small Java Library that provide reusable utilities Classes");
		System.out
				.println("and static methods for many common Java programming tasks. Also this");
		System.out
				.println("package contains many small Java programs that provide text and file");
		System.out
				.println("management. All programs have both Unix and Windows wrapper scripts");
		System.out
				.println("that start the actual Java program. Enter -h or --help to any program to get");
		System.out.println("more detail usage.");
		System.out.println("");
		System.out
				.println("This project is created and maintained by Zemian Deng, and it's intended");
		System.out
				.println("as an Open Source project. No specific on any licence yet.");
		System.out
				.println("At this moment, feel free to use, copy or provide feedback");
		System.out.println("");
		System.out
				.println("Any bugs or problem may send direct email to: zemiandeng@gmail.com");
		System.out.println("");
		System.out.println("[options]");
		System.out.println("  --h               Help page");
		System.out.println("  --help            Help page");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out.println("  $Id: Main.java 4 2006-03-16 15:27:19Z zemian $");

		System.exit(1);
	}

	/**
	 * Parse and strip out both short and long options from args array
	 * 
	 * @param args
	 *            Description of the Parameter
	 * @param opt
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] parseOpt(String[] args, Properties opt) {
		if (opt == null) {
			opt = new Properties();
		}
		List argsList = new ArrayList();
		for (int i = 0, maxIndex = args.length; i < maxIndex; i++) {
			String arg = args[i];
			if (arg.startsWith("--")) {
				String[] s = arg.substring(2).split("=");
				if (s.length >= 2) {
					opt.setProperty(s[0], s[1]);
				} else {
					opt.setProperty(s[0], "true");
				}
			} else if (arg.startsWith("-")) {
				String s = arg.substring(1);
				if (s.length() > 1) {
					opt.setProperty(s.substring(0, 1), s.substring(1));
				} else {
					opt.setProperty(s, "true");
				}
			} else {
				argsList.add(arg);
			}
		}

		return (String[]) argsList.toArray(new String[0]);
	}
}
