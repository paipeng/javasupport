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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: FileReplace.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FileReplace {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  FileReplace [options] Path FilePattern TextPattern TextReplace"
				+ "\n  Find TextPattern in all matched files and filereplace it with TextReplace. All"
				+ "\n  Pattern follows Java RE pattern rule." + "\n"
				+ "\n  [options]" + "\n    -h       Help and version."
				+ "\n    -bEXT    Change backup file extension. Def is bak."
				+ "\n    -r       Search Path recursively."
				+ "\n    -d       Dry run. Do not execute any changes." + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ filereplace -d . import IMPORT"
				+ "\n  $ filereplace -backup=bak . import IMPORT"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: FileReplace.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the FileReplace class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 4) {
			printExitHelp();
		}
		boolean isDryrun = opt.isOpt("d");
		boolean isRecursive = opt.isOpt("r");
		String backupExt = opt.getOpt("b", "bak");

		//Proccess arguments
		String path = opt.getArg(0);
		String fileRegex = opt.getArg(1);
		String textRegex = opt.getArg(2);
		String textReplace = opt.getArg(3);

		Pattern textPattern = Pattern.compile(textRegex);
		//System.out.println("[DEBUG] isRecursive " + isRecursive);
		File[] files = FileUtils.globFiles(new File(path), fileRegex,
				isRecursive);
		//System.out.println("[DEBUG] files " +
		// java.util.Arrays.asList(files));
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			long lenCount = firstPattern(new FileInputStream(file), textPattern);
			//System.out.println("[DEBUG] lenCount " + lenCount);
			if (lenCount != -1) {
				InputStream in = null;
				if (isDryrun) {
					in = new FileInputStream(file);
					dryRunReplace(file.getName(), in, textPattern, textReplace);
					continue;
				}

				File bakFile = new File(file.getParentFile(), file.getName()
						+ "." + backupExt);
				FileUtils.copy(file, bakFile);
				OutputStream out = new FileOutputStream(file);
				in = new FileInputStream(bakFile);
				replace(file.getName(), in, out, textPattern, textReplace);
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param in
	 *            Description of the Parameter
	 * @param pattern
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static long firstPattern(InputStream in, Pattern pattern)
			throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String line = null;
		long lenCount = 0;
		long newLineLen = System.getProperty("line.separator").length();
		while ((line = reader.readLine()) != null) {
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				return lenCount;
			}
			lenCount += line.length() + newLineLen;
		}
		return -1;
	}

	/**
	 * Description of the Method
	 * 
	 * @param filename
	 *            Description of the Parameter
	 * @param in
	 *            Description of the Parameter
	 * @param out
	 *            Description of the Parameter
	 * @param pattern
	 *            Description of the Parameter
	 * @param replace
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void replace(String filename, InputStream in,
			OutputStream out, Pattern pattern, String replace) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		PrintWriter writer = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(out)));

		String line = null;
		long lineCount = 0;
		while ((line = reader.readLine()) != null) {
			String newLine = line;
			lineCount++;
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				newLine = matcher.replaceAll(replace);
				System.out.print(filename + ": ");
				System.out.print(lineCount + ": ");
				System.out.println(newLine);
			}

			writer.println(newLine);
		}
		reader.close();
		writer.flush();
	}

	/**
	 * Description of the Method
	 * 
	 * @param filename
	 *            Description of the Parameter
	 * @param in
	 *            Description of the Parameter
	 * @param pattern
	 *            Description of the Parameter
	 * @param replace
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void dryRunReplace(String filename, InputStream in,
			Pattern pattern, String replace) throws Exception {
		BufferedReader reader = new BufferedReader(new InputStreamReader(in));

		String line = null;
		long lineCount = 0;
		while ((line = reader.readLine()) != null) {
			String newLine = line;
			lineCount++;
			Matcher matcher = pattern.matcher(line);
			if (matcher.find()) {
				newLine = matcher.replaceAll(replace);
				System.out.print(filename + ": ");
				System.out.print(lineCount + ": ");
				System.out.println(newLine);
			}
		}
		reader.close();
	}
}
