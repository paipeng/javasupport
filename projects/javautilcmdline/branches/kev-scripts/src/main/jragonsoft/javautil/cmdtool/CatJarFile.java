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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: CatJarFile.java 4 2006-03-16 15:27:19Z zemian $
 */
public class CatJarFile {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  CatJarFile [options] JarFile [FileEntry] [FileEntry ...]"
				+ "\n  Concatnate or print the file content inside a jar/zip file. If no argument"
				+ "\n  is passed, it default to META-INF/MANIFEST.MF." + "\n"
				+ "\n  [options]" + "\n    -h      Help and version." + "\n"
				+ "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ catjarfile lib/javautil.jar"
				+ "\n  $ catjarfile lib/test.jar res/Default.properties" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: CatJarFile.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the CatJarFile class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws IOException {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}

		//Process arguments
		String jarFilename = opt.getArg(0);
		HashSet fileSet = new HashSet();
		if (opt.getArgsCount() > 1) {
			for (int i = 1, maxIndex = opt.getArgsCount(); i < maxIndex; i++) {
				String filename = opt.getArg(i);
				fileSet.add(filename);
			}
		} else {
			fileSet.add("META-INF/MANIFEST.MF");
		}

		int count = 0;
		JarFile jar = new JarFile(jarFilename);
		for (Enumeration e = jar.entries(); e.hasMoreElements();) {
			JarEntry entry = (JarEntry) e.nextElement();
			String entryName = entry.getName();
			if (entry.isDirectory()) {
				continue;
			}

			if (fileSet.contains(entryName)) {
				if (count++ > 0) {
					System.out.println("=== File: " + entryName);
				}
				InputStream in = jar.getInputStream(entry);
				String text = FileUtils.getString(in);
				System.out.println(text);
			}
		}
		jar.close();

		if (count == 0) {
			System.err.println("FileEntry not found!");
		}
	}
}
