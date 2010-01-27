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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.support.GetOpt;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: SystemRepeat.java 4 2006-03-16 15:27:19Z zemian $
 */
public class SystemRepeat {
	private static SimpleDateFormat df = new SimpleDateFormat(
			"MM/dd/yy-HH:mm:ss");

	private static boolean debug;

	private static boolean isDryRun;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  SystemRepeat [options] XNum SystemCall"
				+ "\n  Repeat a system call XNum of times. SystemCall is passed to Java's"
				+ "\n  Runtime.exec() method without ENV setup. Each SystemCall is in"
				+ "\n  separated process, and this program will wait eaach SystemCall to finish"
				+ "\n  before each repeat."
				+ "\n  "
				+ "\n  If 3rd or more arguments are entered, it will be concatnated to SystemCall"
				+ "\n  as it's argument with a whitespace."
				+ "\n"
				+ "\n  [options]"
				+ "\n    --help       Help and version."
				+ "\n    --debug      Turn debug on."
				+ "\n    --dryrun     Only print SystemCall rather than exec them."
				+ "\n    --delay=MSEC Delay millisecs after each SystemCall finishes."
				+ "\n    --stopAfter=MSEC "
				+ "\n                 Override XNum to specify ending after ellapse of millis sec."
				+ "\n    --stopOn=MM/DD/YY-HH:mm:SS "
				+ "\n                 Override XNum to specify ending datetime."
				+ "\n    --startOn=MM/DD/YY-HH:mm:SS "
				+ "\n                 Delay the first SystemCall until specific datetime."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ systemrepeat 10 \"echo TEST\""
				+ "\n  $ systemrepeat 10 echo TEST #should be same as above line."
				+ "\n  $ systemrepeat 10  alphanum 64"
				+ "\n  $ systemrepeat --delay=1500 10  alphanum 64"
				+ "\n  $ systemrepeat --stopAfter=5000 0  alphanum 64"
				+ "\n  $ systemrepeat --stopOn=09/08/05-23:00:00 0  alphanum 64"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: SystemRepeat.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the SystemRepeat class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetLongOpt(args);
		//System.out.println("[DEBUG] " + opt);

		try {
			//Process options
			if (opt.isOpt("help") || opt.getArgsCount() < 2) {
				printExitHelp();
			}
			debug = opt.isOpt("debug");

			long delay = opt.getLongOpt("delay", 0);
			String stopOnStr = opt.getOpt("stopOn", getTimeStamp());
			String startOnStr = opt.getOpt("startOn", getTimeStamp());
			long stopOn = df.parse(stopOnStr).getTime();
			long startOn = df.parse(startOnStr).getTime();
			long stopAfter = opt.getLongOpt("stopAfter", 0);
			isDryRun = opt.isOpt("dryrun");

			//Process args
			int repeat = Integer.parseInt(opt.getArg(0));

			//concat all args as one single system command!
			StringBuffer sb = new StringBuffer();
			for (int i = 1; i < opt.getArgsCount(); i++) {
				sb.append(" " + opt.getArg(i));
			}
			String systemCmd = sb.substring(1);

			if (opt.isOpt("startOn")) {
				long now = System.currentTimeMillis();
				long wait = startOn - now;
				if (wait > 0) {
					debug("Will start on " + startOnStr);
					Thread.sleep(wait);
				}
			}

			if (opt.isOpt("stopOn")) {
				long now = System.currentTimeMillis();
				if (now >= stopOn) {
					System.err.println("StopOn datetime is already overdue!");
				}
				while (now <= stopOn) {
					debug("Repeat until " + stopOnStr + " Time Now: "
							+ df.format(new Date(now)));
					systemCall(systemCmd, delay);

					now = System.currentTimeMillis();
				}
			} else if (opt.isOpt("stopAfter")) {
				long started = System.currentTimeMillis();
				long now = System.currentTimeMillis();
				while ((now - started) <= stopAfter) {
					debug("Repeat until " + stopAfter
							+ " of msecs is passed. Time Left: "
							+ (stopAfter - (now - started)) + " msecs");
					systemCall(systemCmd, delay);

					now = System.currentTimeMillis();
				}
			} else {
				for (int num = 0; num < repeat; num++) {
					debug("Repeat#" + num);
					systemCall(systemCmd, delay);
				}
			}

			debug("Stopped on: " + df.format(new Date()));

		} catch (Exception e) {
			System.err.println("ERROR:");
			e.printStackTrace();
			System.out.println("[DEBUG] opt\n" + opt);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param systemCmd
	 *            Description of the Parameter
	 * @param delay
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void systemCall(String systemCmd, long delay)
			throws Exception {
		long startTime = System.currentTimeMillis();
		debug("SystemCall start: " + df.format(new Date(startTime)));
		if (isDryRun) {
			System.out.println("DryRun SystemCall: " + systemCmd);
		} else {
			Process proc = Runtime.getRuntime().exec(systemCmd);
			int exitValue = proc.waitFor();
			if (exitValue == 0) {
				String line;
				BufferedReader in = new BufferedReader(new InputStreamReader(
						proc.getInputStream()));
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
			} else {
				String line;
				BufferedReader in = new BufferedReader(new InputStreamReader(
						proc.getErrorStream()));
				while ((line = in.readLine()) != null) {
					System.out.println(line);
				}
			}
		}
		long stopTime = System.currentTimeMillis();
		debug("SystemCall stop: " + df.format(new Date(stopTime)));
		debug("SystemCall took: " + (stopTime - startTime) + " msecs");

		if (delay > 0) {
			debug("Delaying " + delay + " millis secs ...");
			Thread.sleep(delay);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param msg
	 *            Description of the Parameter
	 */
	public static void debug(String msg) {
		if (debug) {
			String timestamp = df.format(new Date());
			System.out.println(timestamp + " [DEBUG] " + msg);
			//System.out.println("[DEBUG] " + msg);
		}
	}

	/**
	 * Gets the timeStamp attribute of the SystemRepeat class
	 * 
	 * @return The timeStamp value
	 */
	public static String getTimeStamp() {
		return df.format(new Date());
	}
}
