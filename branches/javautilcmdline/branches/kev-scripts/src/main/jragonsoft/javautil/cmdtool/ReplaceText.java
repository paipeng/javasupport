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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: ReplaceText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class ReplaceText {
	/**
	 * Description of the Method
	 */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  ReplaceText [options] TextPattern TextReplace"
				+ "\n  Use Java RegEx pattern to find and replace all occurance of input text."
				+ "\n  Input text is search with MULTILINE flag turned on!"
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo \"one|two|three\" | replacetext o X"
				+ "\n  $ echo \"one|two|three\" | replacetext \\\\\\| \" \""
				+ "\n  $ echo \"one two three\" | replacetext \" \" \"\\\\\\\\ \""
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: ReplaceText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the ReplaceText class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//System.out.println(opt);
		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 2) {
			printExitHelp();
		}

		String textPattern = opt.getArg(0);
		String textReplace = opt.getArg(1);
		String text = SystemUtils.getInputString();

		//String[] elements = text.split(SystemUtils.LINE_SEP);
		Pattern p = Pattern.compile(textPattern, Pattern.MULTILINE);
		Matcher m = p.matcher(text);
		String ret = m.replaceAll(textReplace);
		System.out.print(ret);
	}
}
