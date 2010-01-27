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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: ChooseText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class ChooseText {

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  ChooseText [options] [LineIndex [LineIndex] ...]"
				+ "\n  Choose a specific line number(index) from input. Line number is zero"
				+ "\n  based index, and default to zero."
				+ "\n  Input are taking from Standard Input. Useful for piping commands."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h        Help and version."
				+ "\n    -rRandNum Choose a RandNum of lines from input randomly."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | choosetext"
				+ "\n  $ echo -e 'one\\ntwo\\nthree' | choosetext 1 2"
				+ "\n  $ ls -1 /tmp | choosetext -r1" + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: ChooseText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the ChooseText class
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
		String text = SystemUtils.getInputString();
		String[] choices = text.split(SystemUtils.LINE_SEP);
		List choiceList = Arrays.asList(choices);

		if (opt.isOpt("r")) {
			int numOfSelection = opt.getIntOpt("r", 1);

			if (numOfSelection > choiceList.size()) {
				numOfSelection = choiceList.size();
			}
			Collections.shuffle(choiceList);
			for (int i = 0; i < numOfSelection; i++) {
				System.out.println(choiceList.get(i));
			}
		} else {
			for (int i = 0; i < opt.getArgsCount(); i++) {
				String arg = opt.getArg(i).trim();
				try {
					int index = Integer.parseInt(arg);
					System.out.println(choiceList.get(index));
				} catch (Exception e) {
				}
			}
		}
	}
}
