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

import java.util.Date;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.support.Printf;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: TimeIt.java 4 2006-03-16 15:27:19Z zemian $
 */
public class TimeIt {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  TimeIt [options] SystemCmd [SystemCmd ...]"
				+ "\n  Benchmark and print summary of time took to execute each SystemCmd."
				+ "\n  This program is single threaded and run SystemCmd one at a time."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h           Help and version."
				+ "\n    -d           Print detail of times for each repeat."
				+ "\n    -t           Run SystemCmd in mutithreaded mode."
				+ "\n    -rNum        Repeat Num of times for each SystemCmd. Default to 1."
				+ "\n    -p           Print output of SytemCmd." + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ timeit 1 'echo \"one two three\"'"
				+ "\n  $ timeit 50 'echo \"one two\"' 'echo \"one two three\"'"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: TimeIt.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the TimeIt class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}
		boolean isThreadedMode = opt.isOpt("t");
		boolean isPrintOutput = opt.isOpt("p");
		boolean isDetail = opt.isOpt("d");
		int numRepeat = opt.getIntOpt("r", 1);
		int cmdCount = opt.getArgsCount();

		SystemCmdTask[] runs = new SystemCmdTask[cmdCount];
		Thread[] threads = new Thread[cmdCount];
		for (int i = 0; i < cmdCount; i++) {
			String syscmd = opt.getArg(i);
			System.out.println("Running SysCmd#" + i + ": " + syscmd);
			runs[i] = new SystemCmdTask(syscmd, numRepeat);
			threads[i] = new Thread(runs[i], "SysCmd#" + i);
			if (isThreadedMode) {
				threads[i].start();
			} else {
				threads[i].run();
			}
		}

		//Print output if nessacery
		for (int i = 0; i < cmdCount; i++) {
			if (isThreadedMode) {
				try {
					threads[i].join();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			if (isPrintOutput) {
				for (int count = 0; count < numRepeat; count++) {
					String output = runs[i].getResponse(count);
					System.out.println("=== SystemCmd#" + i + " Repeat#"
							+ count + " Output ===");
					System.out.println(output);
				}
			}
		}

		//Print reports.
		System.out.println();
		if (isDetail) {
			System.out.println("=== TimeIt Summary( in millis secs) ===");
			for (int i = 0; i < cmdCount; i++) {
				String syscmd = opt.getArg(i);

				System.out.println("SysCmd#" + i + " " + syscmd);
				for (int count = 0; count < numRepeat; count++) {
					long ellapsed = runs[i].getEllapsedTime(count);
					String start = new Date(runs[i].getStartTime(count))
							.toString();
					String stop = new Date(runs[i].getStopTime(count))
							.toString();
					Printf.out("  %-15s %-15s %-15s %-15s\n", Printf.add(
							"Repeat#" + count).add("Start=" + start).add(
							"Stop=" + stop).add("Ellapsed=" + ellapsed));
				}
			}
		}

		System.out.println("=== TimeIt Summary( in millis secs) ===");
		for (int i = 0; i < cmdCount; i++) {
			//String syscmd = opt.getArg(i);
			long total = runs[i].getTotalTime();
			double average = runs[i].getAverageTime();
			Printf.out("%-15s %-15s %-15s\n", Printf.add("SysCmd#" + i).add(
					"Total=" + total).add("Avg=" + average));
		}
	}

	/**
	 * Utility program. Use -h or --help option in command line to see help
	 * page.
	 * 
	 * @author zemian
	 * @version $Id: TimeIt.java 4 2006-03-16 15:27:19Z zemian $
	 */
	public static class SystemCmdTask implements Runnable {
		String syscmd;

		String[] response;

		long[] startTime;

		long[] stopTime;

		int numRepeat;

		/**
		 * Constructor for the SystemCmdTask object
		 * 
		 * @param syscmd
		 *            Description of the Parameter
		 * @param numRepeat
		 *            Description of the Parameter
		 */
		public SystemCmdTask(String syscmd, int numRepeat) {
			this.syscmd = syscmd;
			this.numRepeat = numRepeat;
			response = new String[numRepeat];
			startTime = new long[numRepeat];
			stopTime = new long[numRepeat];
		}

		/** Main processing method for the SystemCmdTask object */
		public void run() {
			for (int i = 0; i < numRepeat; i++) {
				startTime[i] = System.currentTimeMillis();
				response[i] = SystemUtils.exec(syscmd);
				stopTime[i] = System.currentTimeMillis();
				//System.out.print(".");
			}
		}

		/**
		 * Gets the ellapsedTime attribute of the SystemCmdTask object
		 * 
		 * @param index
		 *            Description of the Parameter
		 * @return The ellapsedTime value
		 */
		public long getEllapsedTime(int index) {
			return stopTime[index] - startTime[index];
		}

		/**
		 * Gets the startTime attribute of the SystemCmdTask object
		 * 
		 * @param index
		 *            Description of the Parameter
		 * @return The startTime value
		 */
		public long getStartTime(int index) {
			return startTime[index];
		}

		/**
		 * Gets the stopTime attribute of the SystemCmdTask object
		 * 
		 * @param index
		 *            Description of the Parameter
		 * @return The stopTime value
		 */
		public long getStopTime(int index) {
			return stopTime[index];
		}

		/**
		 * Gets the sysCmd attribute of the SystemCmdTask object
		 * 
		 * @return The sysCmd value
		 */
		public String getSysCmd() {
			return syscmd;
		}

		/**
		 * Gets the response attribute of the SystemCmdTask object
		 * 
		 * @param index
		 *            Description of the Parameter
		 * @return The response value
		 */
		public String getResponse(int index) {
			return response[index];
		}

		/**
		 * Gets the totalTime attribute of the SystemCmdTask object
		 * 
		 * @return The totalTime value
		 */
		public long getTotalTime() {
			long total = 0;
			for (int i = 0; i < numRepeat; i++) {
				//System.out.println("[DEBUG] stopTime["+i+"]" + stopTime[i]);
				//System.out.println("[DEBUG] startTime["+i+"]" +
				// startTime[i]);
				total += (stopTime[i] - startTime[i]);
			}
			return total;
		}

		/**
		 * Gets the averageTime attribute of the SystemCmdTask object
		 * 
		 * @return The averageTime value
		 */
		public double getAverageTime() {
			return getTotalTime() / numRepeat;
		}
	}
}
