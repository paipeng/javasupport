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

import java.io.File;
import java.util.ArrayList;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.ArrayUtils;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: QuoteJavaText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class QuoteJavaText {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  QuoteJavaText [options] TextValue"
				+ "\n  One of Java disadvantage is missing HEREDOC capability. and Quoting"
				+ "\n  larget amount of pasting text in annoying when converting into String."
				+ "\n  This utility will convert TextValue and reformats it, and"
				+ "\n  print out the proper Java String literal for quick pasting."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -fFILENAME  Override TextValue from a file."
				+ "\n    -s          Override TextValue from a Standard Input."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ quotejavatext 'test \"quote\"'"
				+ "\n  $ quotejavatext 'slash \\me out.'"
				+ "\n  $ quotejavatext 'slash \\me out.' '\\test\\'"
				+ "\n  $ quotejavatext <<HERE"
				+ "\n> \"\\\"Java\\\"|'Maddness'\" \"Quoting in \\\"Java\\\" is 'Maddness'.\""
				+ "\n>HERE" + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: QuoteJavaText.java 4 2006-03-16 15:27:19Z zemian $"
				+ "\n";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the QuoteJavaText class
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

		//int col = opt.getIntOpt("c", 80);

		String[] lines;
		if (opt.getArgsCount() >= 1) {
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < opt.getArgsCount(); i++) {
				if (i > 0) {
					sb.append(FileUtils.LINE_SEP);
				}
				sb.append(opt.getArg(i));
			}
			lines = StringUtils.split(sb.toString(), FileUtils.LINE_SEP);
		} else if (opt.isOpt("f")) {
			lines = FileUtils.getLines(new File(opt.getOpt("f", "input.txt")));
		} else {
			lines = SystemUtils.getInputLines();
		}

		String[] javaStrs = quoteJavaText(lines);
		for (int i = 0, maxIndex = javaStrs.length; i < maxIndex; i++) {
			if (i > 0) {
				System.out.print("    + ");
			}
			System.out.println(javaStrs[i]);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param line
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String quoteJavaText(String line) {
		return quoteJavaText(new String[] { line })[0];
	}

	/**
	 * Description of the Method
	 * 
	 * @param lines
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] quoteJavaText(String[] lines) {
		ArrayList result = new ArrayList();
		for (int i = 0, maxIndex = lines.length; i < maxIndex; i++) {
			String javaStr = lines[i];
			javaStr = StringUtils.replace(javaStr, "\\", "\\\\");
			javaStr = StringUtils.replace(javaStr, "\"", "\\\"");
			result.add("\"" + javaStr + "\"");
		}

		return ArrayUtils.toStringArray(result);
	}
}