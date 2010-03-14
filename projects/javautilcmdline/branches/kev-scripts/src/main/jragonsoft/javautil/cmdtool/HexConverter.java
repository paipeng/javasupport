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
import java.io.FileInputStream;
import java.io.InputStream;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.support.Printf;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.StringUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: HexConverter.java 4 2006-03-16 15:27:19Z zemian $
 */
public class HexConverter {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  HexConverter [options] File"
				+ "\n  Read a file and print it's content in Hex. Each byte is printed "
				+ "\n  with two hex values and a space."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h           Help and version."
				+ "\n    -cCOL        Specify number of Column/BYTE per line. Default is 20."
				+ "\n    -s           Read from STANDARD INPUT rather than file."
				+ "\n    -n           Do not print line numbers"
				+ "\n    -dVAL        Convert a decimal VAL to hex" + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ hexconverter test.txt"
				+ "\n  $ printf \"\\r\\n\" | hexconverter -s -c1" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: HexConverter.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the HexConverter class
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

		if (opt.isOpt("d")) {
			String num = opt.getOpt("d", null);
			if (num == null) {
				throw new Exception("You need a number for -d option.");
			}
			System.out.println(Integer.toHexString(Integer.parseInt(num))
					.toUpperCase());
			System.exit(0);
		}

		boolean isPrintLineNo = !opt.isOpt("n");
		int width = opt.getIntOpt("c", 20);
		InputStream in = null;
		if (opt.isOpt("s")) {
			in = System.in;
		} else {
			if (opt.getArgsCount() < 1) {
				printExitHelp();
			}
			in = new FileInputStream(new File(opt.getArg(0)));
		}

		String text = FileUtils.getString(in);
		byte[] bytes = text.getBytes();
		String output = StringUtils.toHexString(bytes, 0, bytes.length, width);

		if (isPrintLineNo) {
			String[] lines = StringUtils.split(output, FileUtils.LINE_SEP);
			if (width == 2) {
				for (int i = 0; i < lines.length; i++) {
					Printf.out("%08d: %s\n", Printf.add(i).add(lines[i]));
				}
			} else {
				for (int i = 0; i < lines.length; i++) {
					int from = i * width + 1;
					int to = from + (lines[i].length()) / 3;
					Printf.out("%08d-%08d: %s\n", Printf.add(from).add(to).add(
							lines[i]));
				}
			}
		} else {
			System.out.println(output);
		}
	}
}
