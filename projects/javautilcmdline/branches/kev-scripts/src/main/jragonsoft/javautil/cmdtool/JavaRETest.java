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
import java.util.Arrays;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.RegexUtils;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: JavaRETest.java 4 2006-03-16 15:27:19Z zemian $
 */
public class JavaRETest {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  JavaRETest [options] PatternText InputText"
				+ "\n  Utility to test Java regular expression and help you build your Pattern"
				+ "\n  string. This program by default will do a matcher's find() against the"
				+ "\n  InputText. If successful, all matching and it's group will be printed."
				+ "\n  "
				+ "\n  When displaying results, Group#0 is always the entire matched string."
				+ "\n  All input are read in as one big string!"
				+ "\n  "
				+ "\n  You should know that when constructing string patterns, Unix shell only need"
				+ "\n  single slash in single quote, while Java need double slash to create a single"
				+ "\n  slash. You may use the output of this program to create your Java pattern string."
				+ "\n  See QuoteJavaText for more on quoting string in Java."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h         Help and version."
				+ "\n    -x         Split input into lines."
				+ "\n    -e         Escape all PatternText."
				+ "\n    -mMatch    Change method to matching entire input."
				+ "\n    -mLook     Change method to matching from begining of the input."
				+ "\n    -mFind     Change method to matching any where in input. Default"
				+ "\n    -mReplace=STRING"
				+ "\n               Change method to replace all occurance with STRING."
				+ "\n    -fFILE     Override InputText with a FILE input."
				+ "\n    -s         Override InputText with Standard Input"
				+ "\n    -M         Compile PatternText with MULTILINE flag."
				+ "\n    -C         Compile PatternText with CASE_INSENSITIVE flag."
				+ "\n    -D         Compile PatternText with DOTALL flag."
				+ "\n    -U         Compile PatternText with UNICODE_CASE flag."
				+ "\n    -E         Compile PatternText with CANON_EQ flag."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ javaretest 'Zemian' 'Zemian Deng'"
				+ "\n  $ javaretest 'Deng' 'Zemian Deng'"
				+ "\n  $ javaretest '(\\w+)\\s+(class)\\s+([a-zA-Z_]\\w+)' 'public class JavaRETest {'"
				+ "\n  $ javaretest '(?<=^\\s*|[^\\$])\\$\\{(.+?)\\}' 'Variable ${foo} match, but not $${bar}.'"
				+ "\n  $ javaretest \"\\\"Java\\\"|'Maddness'\" \"Quoting in \\\"Java\\\" is 'Maddness'.\""
				+ "\n  $ javaretest -mReplace='\\\\$1' '([^a-zA-z0-9]|\\\\|\\[|\\]|\\^)' '{}[]\\|\"/?><()*&^%$#@!~`ABCxyz0123'"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: JavaRETest.java 4 2006-03-16 15:27:19Z zemian $"
				+ "\n";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the JavaRETest class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		ArrayList inputs = new ArrayList();

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}
		String matchMethod = opt.getOpt("m", "Find");
		String replaceString = null;
		if (matchMethod.startsWith("Replace")) {
			replaceString = matchMethod.substring("Replace".length() + 1);
			matchMethod = "Replace";
		}
		String filename = opt.getOpt("f", null);

		//Process arguments
		String patternText = opt.getArg(0);

		if (opt.isOpt("e")) {
			patternText = RegexUtils.escapeRegex(patternText);
		}

		String inputText = null;
		if (opt.isOpt("s")) {
			inputText = SystemUtils.getInputString();
		} else if (filename != null) {
			inputText = FileUtils.getString(new File(filename));
		} else {
			if (opt.getArgsCount() < 2) {
				printExitHelp();
			}
			inputText = opt.getArg(1);
		}

		if (opt.isOpt("x")) {
			inputs.addAll(Arrays.asList(StringUtils.split(inputText,
					FileUtils.LINE_SEP)));
		} else {
			inputs.add(inputText);
		}

		int flag = 0;
		if (opt.isOpt("M")) {
			flag |= Pattern.MULTILINE;
		}
		if (opt.isOpt("C")) {
			flag |= Pattern.CASE_INSENSITIVE;
		}
		if (opt.isOpt("U")) {
			flag |= Pattern.UNICODE_CASE;
		}
		if (opt.isOpt("D")) {
			flag |= Pattern.DOTALL;
		}
		if (opt.isOpt("E")) {
			flag |= Pattern.CANON_EQ;
		}

		System.out.println("=== Method: " + matchMethod);
		System.out.println("=== Pattern String: " + patternText);
		String patternStr = QuoteJavaText.quoteJavaText(patternText);
		System.out.println("=== Java Pattern String: " + patternStr);
		for (Iterator itr = inputs.iterator(); itr.hasNext();) {
			inputText = (String) itr.next();

			Pattern pattern = Pattern.compile(patternText, flag);
			Matcher matcher = pattern.matcher(inputText);
			int matchedCount = 0;

			System.out.println("=== Original Text:");
			System.out.println(inputText);

			if (matchMethod.equals("Replace")) {
				System.out.println("=== Replace String: " + replaceString);
				String replacePatternStr = QuoteJavaText
						.quoteJavaText(replaceString);
				System.out.println("=== Java Replace String: "
						+ replacePatternStr);
				System.out.println("=== Replacement:");
				System.out.println(matcher.replaceAll(replaceString));
			} else if (matchMethod.equals("Match") && matcher.matches()) {
				matchedCount++;
				//NO while loop, it's either match or not.
				printMatchedItems(matcher, inputText);
			} else if (matchMethod.equals("Look")) {
				while (matcher.lookingAt()) {
					matchedCount++;
					printMatchedItems(matcher, inputText);
				}
			} else if (matchMethod.equals("Find")) {
				while (matcher.find()) {
					matchedCount++;
					printMatchedItems(matcher, inputText);
				}
			}
			System.out.println("=== " + matchedCount + " matche(s) found.");
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param matcher
	 *            Description of the Parameter
	 * @param inputText
	 *            Description of the Parameter
	 */
	public static void printMatchedItems(Matcher matcher, String inputText) {
		System.out.print("=== Got matched.");
		System.out.println(" GroupCount: " + matcher.groupCount());
		for (int i = 0; i <= matcher.groupCount(); i++) {
			System.out.print("Group#" + i + ": " + matcher.group(i));
			System.out.print(" (Start: " + matcher.start(i));
			System.out.print(" End: " + matcher.end(i) + ")");
			//String substr =inputText.substring(matcher.start(i),
			// matcher.end(i));
			//System.out.print(" SubStr: " + substr);
			System.out.println();
		}
	}
}