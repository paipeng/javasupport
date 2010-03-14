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

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.support.Printf;
import jragonsoft.javautil.util.ArrayUtils;
import jragonsoft.javautil.util.StringUtils;
import jragonsoft.javautil.util.SystemUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: NumberConverter.java 4 2006-03-16 15:27:19Z zemian $
 */
public class NumberConverter {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  NumberConverter [options] Value [Value ...]"
				+ "\n  Convert Decimal Value to Hex's equivalent."
				+ "\n  The Value must be between -9223372036854775808 to 9223372036854775807,"
				+ "\n  or in Hex: 0x8000000000000000 to 0x7FFFFFFFFFFFFFFF."
				+ "\n"
				+ "\n  NOTE: You need to escape negative number with slash '\\-'."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -l      Show java number's limits."
				+ "\n    -x      Make input Value in Hex form."
				+ "\n    -o      Make input Value in Octal form."
				+ "\n    -b      Make input Value in Binaray form."
				+ "\n    -dx     Display value in hex form."
				+ "\n    -do     Display value in octal form."
				+ "\n    -db     Display value in binary form."
				+ "\n    -dd     Display value in decimal form."
				+ "\n    -s      Take input from Standard Input instead."
				+ "\n    -a      Display all conversion: Binary, Oct, Dec, & Hex"
				+ "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ numberconverter 32"
				+ "\n  $ numberconverter -a 32 126"
				+ "\n  $ numberconverter -x 20 7F"
				+ "\n  $ numberconverter '\\-9223372036854775808'"
				+ "\n  $ numberrange 32 | numberconverter -s -d"
				+ "\n  $ numberrange 32 | numberconverter -s -d -a"
				+ "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: NumberConverter.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the NumberConverter class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}

		if (opt.isOpt("l")) {
			System.out.println("Byte: " + +Byte.MIN_VALUE + " to "
					+ Byte.MAX_VALUE);
			System.out.println("Short: " + +Short.MIN_VALUE + " to "
					+ Short.MAX_VALUE);
			System.out.println("Integer: " + +Integer.MIN_VALUE + " to "
					+ Integer.MAX_VALUE);
			System.out.println("Long: " + +Long.MIN_VALUE + " to "
					+ Long.MAX_VALUE);
			System.out.println("Float: " + +Float.MIN_VALUE + " to "
					+ Float.MAX_VALUE);
			System.out.println("Double: " + +Double.MIN_VALUE + " to "
					+ Double.MAX_VALUE);
			System.exit(0);
		}

		if (opt.isOpt("s")) {
			ArrayList valueList = new ArrayList();
			String[] lines = SystemUtils.getInputLines();
			for (int i = 0; i < lines.length; i++) {
				String[] nums = lines[i].split("\\s+");
				valueList.addAll(Arrays.asList(nums));
			}

			String[] valueArray = ArrayUtils.toStringArray(valueList);
			if (opt.isOpt("a")) {
				convertNumDisplayAll(opt, valueArray);
			} else {
				convertNum(opt, valueArray);
			}
			System.exit(0);
		}

		if (opt.getArgsCount() < 1) {
			printExitHelp();
		}

		if (opt.isOpt("a")) {
			convertNumDisplayAll(opt, opt.getArgs());
		} else {
			convertNum(opt, opt.getArgs());
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param nums
	 *            Description of the Parameter
	 */
	public static void convertNum(GetOpt opt, String[] nums) {
		long value = -1;
		for (int i = 0; i < nums.length; i++) {
			if (opt.isOpt("x")) {
				value = Long.parseLong(nums[i], 16);
			} else if (opt.isOpt("b")) {
				value = Long.parseLong(nums[i], 2);
			} else if (opt.isOpt("o")) {
				value = Long.parseLong(nums[i], 8);
			} else {
				value = Long.parseLong(nums[i]);
			}

			String convertedValue = null;

			if (opt.getOpt("d", "x").equals("o")) {
				convertedValue = Long.toOctalString(value);
			} else if (opt.getOpt("d", "x").equals("b")) {
				convertedValue = Long.toBinaryString(value);
			} else if (opt.getOpt("d", "x").equals("d")) {
				convertedValue = "" + value;
			} else if (opt.getOpt("d", "x").equals("x")) {
				convertedValue = Long.toHexString(value).toUpperCase();
			} else {
				convertedValue = Long.toHexString(value).toUpperCase();
			}

			System.out.print(convertedValue);
			if (i < nums.length - 1) {
				System.out.print(" ");
			}
		}
		System.out.println();
	}

	/**
	 * Description of the Method
	 * 
	 * @param opt
	 *            Description of the Parameter
	 * @param nums
	 *            Description of the Parameter
	 */
	public static void convertNumDisplayAll(GetOpt opt, String[] nums) {
		Printf.out("%-20s %-17s %-22s %s\n", Printf.add("Decimal").add("Hex")
				.add("Octal").add("Binary"));
		long value = -1;
		for (int i = 0; i < nums.length; i++) {
			if (opt.isOpt("x")) {
				value = Long.parseLong(nums[i], 16);
			} else if (opt.isOpt("b")) {
				value = Long.parseLong(nums[i], 2);
			} else if (opt.isOpt("o")) {
				value = Long.parseLong(nums[i], 8);
			} else {
				value = Long.parseLong(nums[i]);
			}

			String bin = Long.toBinaryString(value);
			bin = StringUtils.padFrontChar(bin, 32, '0');
			String oct = Long.toOctalString(value);
			String hex = Long.toHexString(value).toUpperCase();

			Printf.out("%-20d %-17s %-22s %s\n", Printf.add(value).add(hex)
					.add(oct).add(bin));
		}
	}
}
