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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.support.Printf;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: AsciiChar.java 4 2006-03-16 15:27:19Z zemian $
 */
public class AsciiChar {

	/** Description of the Field */
	public static int BASIC_LATIN_CHARS_COUNT = 128;

	/** Description of the Field */
	public static String[] basicLatinChars = new String[0xFF + 1];

	private static boolean isPrintRaw = false;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  AsciiChar [options] [Value] [Value ...]"
				+ "\n  Convert hex Value to a printable Ascii Symbol. If Value is"
				+ "\n  not printable, it will have a mapped Lable instead. For label description,"
				+ "\n  see http://www.unicode.org/charts/PDF/U0000.pdf"
				+ "\n"
				+ "\n  NOTE: Altough a two hex digits give you 00 to FF, but keeps these in mind:"
				+ "\n          - Unicode's Basic Lantin Char Set is ranged from 0x00 to 0x7F(0 to 127)."
				+ "\n          - Printable ASCII chars table is ranged from 0x20 to 0x7E(32 to 126)."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -s      Take Value from Standard Input instead."
				+ "\n    -cNUM   Specify the number of column to display. Def 3."
				+ "\n    -p      Print raw char without mapped Labels or column formats."
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ asciichar"
				+ "\n  $ asciichar -p 4A 61 76 61 20 69 73 20 65 61 73 79 21 0A"
				+ "\n  $ echo -ne \"test\\none\" | hexconverter -s -n | asciichar -s -p"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: AsciiChar.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the AsciiChar class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		initBasicLatinChars();
		//System.out.println("[DEBUG] basicLatinChars " +
		// java.util.Arrays.asList(basicLatinChars));

		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}

		int col = opt.getIntOpt("c", 3);
		isPrintRaw = opt.isOpt("p");

		if (opt.isOpt("s")) {
			ArrayList valueList = new ArrayList();
			String[] lines = SystemUtils.getInputLines();
			for (int i = 0; i < lines.length; i++) {
				String[] nums = lines[i].split("\\s+");
				valueList.addAll(Arrays.asList(nums));
			}

			int valueChars[] = new int[valueList.size()];
			int i = 0;
			for (Iterator itr = valueList.iterator(); itr.hasNext();) {
				String s = (String) itr.next();
				valueChars[i++] = Integer.parseInt(s, 16);
			}
			printChars(valueChars, 1);
			System.out.println();
			System.exit(0);
		}

		if (opt.getArgsCount() == 0) {
			int[] allChars = new int[BASIC_LATIN_CHARS_COUNT];
			for (int i = 0; i < BASIC_LATIN_CHARS_COUNT; i++) {
				allChars[i] = i;
			}
			printChars(allChars, col);
			System.out.println();
			System.exit(0);
		}

		int[] valueChars = new int[opt.getArgsCount()];
		for (int i = 0; i < opt.getArgsCount(); i++) {
			valueChars[i] = Integer.parseInt(opt.getArg(i), 16);
		}
		printChars(valueChars, col);
	}

	/** Description of the Method */
	private static void initBasicLatinChars() {
		String[] nonPrint = { "NUL", "SOH", "STX", "ETX", "EOT", "ENQ", "ACK",
				"BEL", "BS", "HT", "LF", "VT", "FF", "CR", "SO", "SI", "DLE",
				"DC1", "DC2", "DC3", "DC4", "NAK", "SYN", "ETB", "CAN", "EM",
				"SUB", "ESC", "FS", "GS", "RS", "US", "SP" };
		System.arraycopy(nonPrint, 0, basicLatinChars, 0, nonPrint.length);

		for (int i = 33; i <= 126; i++) {
			basicLatinChars[i] = new Character((char) i).toString();
		}
		basicLatinChars[127] = "DEL";

		for (int i = 128; i <= 0xFF; i++) {
			basicLatinChars[i] = "???";
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param range
	 *            Description of the Parameter
	 * @param col
	 *            Description of the Parameter
	 */
	private static void printChars(int[] range, int col) {
		if (isPrintRaw) {
			col = 1;
		}
		int row = range.length / col + 1;
		for (int r = 0; r < row; r++) {
			for (int c = 0; c < col; c++) {
				int index = (r + row * c);
				if (index >= range.length) {
					return;
				}

				if (isPrintRaw) {
					//System.out.println("[DEBUG] range[index] " +
					// range[index]);
					System.out.print(new Character((char) range[index]));
					continue;
				}

				int charIdx = range[index];
				String charStr = basicLatinChars[charIdx];

				String bin = StringUtils.padFrontChar(Integer
						.toBinaryString(charIdx), 8, '0');
				int dec = charIdx;
				String hex = Integer.toHexString(charIdx).toUpperCase();
				if (hex.length() == 1) {
					hex = "0" + hex;
				}

				Printf.out("%s %3d %s %3s", Printf.add(bin).add(dec).add(hex)
						.add(charStr));
				if (c < col - 1) {
					System.out.print("     ");
				}
			}
			if (!isPrintRaw) {
				System.out.println();
			}
		}
	}
}
