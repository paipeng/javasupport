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

import java.io.FileInputStream;
import java.io.InputStream;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.support.Printf;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: IsText.java 4 2006-03-16 15:27:19Z zemian $
 */
public class IsText {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:" + "\n  IsText [options] File [File ...]"
				+ "\n  Determine whether File is all text or not."
				+ "\n  We quality the following char values as ALL TEXT:"
				+ "\n  * Empty file (content length is zero)"
				+ "\n  * First byte can not be ZERO"
				+ "\n  * 32 <= CHAR <=127, plus CR, LF, HT, BS" + "\n"
				+ "\n  [options]" + "\n    -h           Help and version."
				+ "\n    -s           Take Standard Input as input text" + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ istext file.dat" + "\n  $ istext file.dat file2.dat"
				+ "\n  $ cat file.dat | istext -s" + "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: IsText.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the IsText class
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

		if (opt.isOpt("s")) {
			isText("STDIN", System.in);
			System.exit(0);
		}

		if (opt.getArgsCount() < 1) {
			printExitHelp();
		}

		for (int i = 0; i < opt.getArgsCount(); i++) {
			String filename = opt.getArg(i);
			isText(filename, new FileInputStream(filename));
		}
	}

	/**
	 * Gets the text attribute of the IsText class
	 * 
	 * @param filename
	 *            Description of the Parameter
	 * @param in
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	private static void isText(String filename, InputStream in)
			throws Exception {
		int textAmount = 100;
		int binAmount = 0;
		String isText = "ES";

		//Process file
		long byteCount = 0;
		long fileLength = 0;

		byte buffer[] = new byte[FileUtils.MAX_READ];
		int len = in.read(buffer, 0, 1);

		if (len == -1) {
			throw new Exception("Can't read file!");
		}
		fileLength++;

		//If first byte is ZERO, then it's consider a binaray.
		if (buffer[0] == '\0') {
			byteCount++;
		}

		while ((len = in.read(buffer, 0, FileUtils.MAX_READ)) != -1) {
			fileLength += len;
			for (int i = 0; i < len; i++) {
				byte b = buffer[i];
				//Check byte to see if falls into printable chars.+ 127(DEL),
				// LF, CR, HT & BS
				if (!((b >= 32 && b <= 127) || (b == '\n') || (b == '\r')
						|| (b == '\t') || (b == '\b'))) {
					byteCount++;
				}
			}
		}

		//If file is empty, it's considered a TEST file.
		if (fileLength != 0) {
			binAmount = Math.round(((float) byteCount / fileLength) * 100);
			textAmount = 100 - binAmount;
			isText = textAmount == 100 ? "YES" : "NO";
		}

		Printf.out("IsText? %-3s (%3d%%-TEXT %3d%%-BINARY) %s \n", Printf.add(
				isText).add(textAmount).add(binAmount).add(filename));
	}
}
