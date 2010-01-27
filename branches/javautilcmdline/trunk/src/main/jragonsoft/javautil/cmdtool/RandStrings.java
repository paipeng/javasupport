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

import jragonsoft.javautil.support.GetOpt;

/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: RandStrings.java 4 2006-03-16 15:27:19Z zemian $
 */
public class RandStrings {
	/** Description of the Field */
	public final static String NUMERIC = "0123456789";

	/** Description of the Field */
	public final static String ALPHABET_LO = "abcdefghijklmnopqrstuvwxyz";

	/** Description of the Field */
	public final static String ALPHABET_UP = ALPHABET_LO.toUpperCase();

	/** Description of the Field */
	public final static String ALPHABET = ALPHABET_LO + ALPHABET_UP;

	/** Description of the Field */
	public final static String ALPHABET_NUMBERIC = ALPHABET + NUMERIC;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  RandStrings [options] SubCommand"
				+ "\n  Print random string that can be choosen from group of letters, "
				+ "\n  numbers, and/or combination of both. Note that SubCommand is not"
				+ "\n  option, so do NOT use dash in front!"
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n"
				+ "\n  [SubCommand]"
				+ "\n    ascii         Print alphebet letters plus 0-9."
				+ "\n    ascii_lo      Print alphebet letters in lower case."
				+ "\n    ascii_up      Print alphebet letters in upper case."
				+ "\n    alpha LEN     Print LEN of random letters from alphabet."
				+ "\n    number LEN    Print LEN of random digits from 0-9."
				+ "\n    alphanum LEN  Print LEN of random letters from alphabet plus 0-9."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ randstrings ascii"
				+ "\n  $ randstrings number 64"
				+ "\n  $ randstrings alphanum 64"
				+ "\n  $ for x in `numberrange 10`; do randstrings alphanum 64; done"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: RandStrings.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the RandStrings class
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

		//Process arguments
		if (opt.getArg(0).equalsIgnoreCase("ascii_lo")) {
			System.out.println(ALPHABET_LO);
		} else if (opt.getArg(0).equalsIgnoreCase("ascii_up")) {
			System.out.println(ALPHABET_UP);
		} else if (opt.getArg(0).equalsIgnoreCase("ascii")) {
			System.out.println(ALPHABET_NUMBERIC);
		} else if (opt.getArg(0).equalsIgnoreCase("alpha")) {
			if (opt.getArgsCount() < 2) {
				printExitHelp();
			}
			int len = Integer.parseInt(opt.getArg(1));
			System.out.println(getRandString(ALPHABET, len));
		} else if (opt.getArg(0).equalsIgnoreCase("number")) {
			if (opt.getArgsCount() < 2) {
				printExitHelp();
			}
			int len = Integer.parseInt(opt.getArg(1));
			System.out.println(getRandString(NUMERIC, len));
		} else if (opt.getArg(0).equalsIgnoreCase("alphanum")) {
			if (opt.getArgsCount() < 2) {
				printExitHelp();
			}
			int len = Integer.parseInt(opt.getArg(1));
			System.out.println(getRandString(ALPHABET_NUMBERIC, len));
		}
	}

	/**
	 * Gets the upperAlphabet attribute of the RandStrings class
	 * 
	 * @param len
	 *            Description of the Parameter
	 * @return The upperAlphabet value
	 */
	public static String getUpperAlphabet(int len) {
		return getRandString(ALPHABET_UP, len);
	}

	/**
	 * Gets the lowerAlphabet attribute of the RandStrings class
	 * 
	 * @param len
	 *            Description of the Parameter
	 * @return The lowerAlphabet value
	 */
	public static String getLowerAlphabet(int len) {
		return getRandString(ALPHABET_LO, len);
	}

	/**
	 * Gets the alphabet attribute of the RandStrings class
	 * 
	 * @param len
	 *            Description of the Parameter
	 * @return The alphabet value
	 */
	public static String getAlphabet(int len) {
		return getRandString(ALPHABET, len);
	}

	/**
	 * Gets the numberic attribute of the RandStrings class
	 * 
	 * @param len
	 *            Description of the Parameter
	 * @return The numberic value
	 */
	public static String getNumberic(int len) {
		return getRandString(NUMERIC, len);
	}

	/**
	 * Gets the alphanum attribute of the RandStrings class
	 * 
	 * @param len
	 *            Description of the Parameter
	 * @return The alphanum value
	 */
	public static String getAlphanum(int len) {
		return getRandString(ALPHABET_NUMBERIC, len);
	}

	/**
	 * Gets the randString attribute of the RandStrings class
	 * 
	 * @param sample
	 *            Description of the Parameter
	 * @param len
	 *            Description of the Parameter
	 * @return The randString value
	 */
	public static String getRandString(String sample, int len) {
		int maxIndex = sample.length() - 1;
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < len; i++) {
			int randIndex = (int) (Math.random() * maxIndex);
			//System.out.println(maxIndex + " "+ randIndex);
			result.append(sample.charAt(randIndex));
		}
		return result.toString();
	}
}