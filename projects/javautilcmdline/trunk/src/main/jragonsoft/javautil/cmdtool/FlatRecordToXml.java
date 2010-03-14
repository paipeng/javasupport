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

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;
import jragonsoft.javautil.util.SystemUtils;
import jragonsoft.javautil.util.XmlUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: FlatRecordToXml.java 4 2006-03-16 15:27:19Z zemian $
 */
public class FlatRecordToXml {
	/** Description of the Method */
	public static void printExitHelp() {
		System.out.println("USAGE:");
		System.out.println("  FlatRecordToXml [options]");
		System.out
				.println("  Convert the Standard Input text in flat record format into xml data.");
		System.out.println("");
		System.out.println("  [options]");
		System.out.println("    -tFILENAME  Override Input text from a file.");
		System.out
				.println("    -lSEP       Override Line separator. Def to System's NL Sep.");
		System.out
				.println("    -fSEP       Override Field separator. Def to |");
		System.out.println("    -n          Do not trim field value.");
		System.out.println("");
		System.out.println("EXAMPLES:");
		System.out.println("  $ flatrecordtoxml -tData.txt");
		System.out.println("  $ flatrecordtoxml -tData.txt -f,");
		System.out
				.println("  $ echo -e 'one|two|three\\n1|2|3' | flatrecordtoxml");
		System.out.println("");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out
				.println("  $Id: FlatRecordToXml.java 4 2006-03-16 15:27:19Z zemian $");
		System.out.println("");

		System.exit(0);
	}

	/**
	 * The main program for the FlatRecordToXml class
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
		String recordSep = opt.getOpt("l", FileUtils.LINE_SEP);
		String fieldSep = opt.getOpt("f", "|");
		boolean isTrimFieldValue = !opt.isOpt("n");

		String input;
		if (opt.isOpt("t")) {
			input = FileUtils.getString(new File(opt.getOpt("t", "input.txt")));
		} else {
			input = SystemUtils.getInputString();
		}

		String xml = XmlUtils.flatRecordToXml(input, recordSep, fieldSep,
				isTrimFieldValue);
		System.out.print(xml);
	}
}