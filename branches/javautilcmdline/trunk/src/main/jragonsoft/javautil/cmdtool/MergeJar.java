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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: MergeJar.java 4 2006-03-16 15:27:19Z zemian $
 */
public class MergeJar {
	private static HashSet newJarFileEntries = new HashSet();

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  MergeJar [options] NewJarFile FromJarFile [FromJarFile ...]"
				+ "\n  Takes one or more jar files and merge them"
				+ "\n  into a single new jar. If there are duplicated classes within"
				+ "\n  packages from different jars, the program will abort."
				+ "\n" + "\n  [options]" + "\n    -h      Help and version."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ mergejar myclient.jar myjdbc.jar" + "\n"
				+ "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: MergeJar.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the MergeJar class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.getArgsCount() < 3) {
			printExitHelp();
		}

		//Process arguments
		String newJarFilename = opt.getArg(0);

		//Make sure new jar file is good.
		File newJarFile = new File(newJarFilename);
		if (newJarFile.isFile() || newJarFile.isDirectory()) {
			System.err.println("NewJarFile already exists!");
			System.exit(-1);
		}

		FileOutputStream out = null;
		JarOutputStream jarout = null;
		try {
			//For each input jar, merge it into new jar
			int jarCount = 0;
			int entryCount = 0;
			out = new FileOutputStream(newJarFile);
			jarout = new JarOutputStream(out);
			for (int i = 1; i < opt.getArgsCount(); i++) {
				entryCount += mergeJar(jarout, opt.getArg(i));
				jarCount++;
			}
			System.out.println(jarCount + " jar files with " + entryCount
					+ " entries have been merged.");
			jarout.flush();
			jarout.close();
		} catch (Exception e) {
			System.err.println("ERROR: " + e);
			if (jarout != null) {
				try {
					jarout.flush();
					jarout.close();
				} catch (IOException ioe) {
					System.err.println("Can't close newJarFile stream???");
				}
			}
			newJarFile.delete();
			System.exit(-2);
		} finally {
			//do nothing.
		}
	}

	/**
	 * Open jarFilename and extract content to merge it into the jar output
	 * stream obj. All original META-INF dirs will be excluded. Also prevent
	 * duplicated jar entry.
	 * 
	 * @param jarout
	 *            Description of the Parameter
	 * @param jarFilename
	 *            Description of the Parameter
	 * @return Number of entry in a jar that were merged.
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static int mergeJar(JarOutputStream jarout, String jarFilename)
			throws Exception {
		int count = 0;
		JarFile jar = new JarFile(jarFilename);
		for (Enumeration e = jar.entries(); e.hasMoreElements();) {
			JarEntry entry = (JarEntry) e.nextElement();
			String entryName = entry.getName();
			if (entry.isDirectory() || entryName.startsWith("META-INF")) {
				continue;
			}

			System.out.println("merging " + entryName);

			if (newJarFileEntries.contains(entryName)) {
				throw new Exception("Duplicated jar entry");
			}

			newJarFileEntries.add(entry.getName());

			jarout.putNextEntry(entry);

			InputStream in = jar.getInputStream(entry);
			int len = 0;
			byte buf[] = new byte[FileUtils.MAX_READ];
			while ((len = in.read(buf, 0, FileUtils.MAX_READ)) != -1) {
				jarout.write(buf, 0, len);
			}
			jarout.closeEntry();
			jarout.flush();
			count++;
		}
		jar.close();
		return count;
	}
}