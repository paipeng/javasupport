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
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jragonsoft.javautil.support.GetOpt;


public class ListJar {
	static String[] jarSuffixes = { ".zip", ".ear", ".war", ".sar", ".jar" };

	static String jarPrefix = "+- ";

	static String entryPrefix = "|  ";

	static boolean showOnlyArchives = false;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  ListJar [options] JarFile"
				+ "\n  List composite jar file content (jar within jar) in a  tree view."
				+ "\n"
				+ "\n  [options]"
				+ "\n    -h      Help and version."
				+ "\n    -n      Do NOT expand composite jar file."
				+ "\n"
				+ "\nCREDITS:"
				+ "\n  This program is based on a Utility from JBoss, The Official Guide."
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: ListJar.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * List the jar contents
	 * 
	 * @param args
	 *            the command line arguments
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetOpt(args);

		if (opt.isOpt("h") || opt.getArgsCount() < 1) {
			printExitHelp();
		}

		showOnlyArchives = opt.isOpt("n");
		String filename = opt.getArg(0);
		FileInputStream fis = new FileInputStream(filename);
		ZipInputStream zis = new ZipInputStream(fis);

		System.out.println(filename);
		listJar(zis, 1);
		System.out.println("Done");
	}

	/**
	 * Description of the Method
	 * 
	 * @param zis
	 *            Description of the Parameter
	 * @param level
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	static void listJar(ZipInputStream zis, int level) throws Exception {
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			processEntry(zis, entry, level);
		}
	}

	/**
	 * Determine whether a filename has one of the jarSuffixes.
	 * 
	 * @param name
	 *            An input filename
	 * @return true if it contains jarSuffixes, false otherwise.
	 */
	static boolean isJar(String name) {
		boolean isJar = false;
		for (int i = jarSuffixes.length - 1; isJar == false && i >= 0; i--) {
			String suffix = jarSuffixes[i];
			isJar |= name.endsWith(suffix);
		}
		return isJar;
	}

	static String getEntryPrefix(int level) {
		StringBuffer sb = new StringBuffer();
		for (int i = level - 2; i >= 0; i--) {
			sb.append(entryPrefix);
		}
		return sb.toString();
	}

	static String getJarPrefix(int level) {
		StringBuffer sb = new StringBuffer();
		for (int i = level - 1; i >= 0; i--) {
			sb.append(jarPrefix);
		}
		return sb.toString();
	}

	/**
	 * Description of the Method
	 * 
	 * @param zis
	 *            Description of the Parameter
	 * @param entry
	 *            Description of the Parameter
	 * @param level
	 *            Description of the Parameter
	 * @exception Exception
	 *                Description of the Exception
	 */
	static void processEntry(ZipInputStream zis, ZipEntry entry, int level)
			throws Exception {
		String name = entry.getName();
		boolean isDirectory = entry.isDirectory();
		if (isDirectory == true) {
			return;
		}

		// See if this is a jar archive
		if (isJar(name) && showOnlyArchives == false) {
			System.out.print(getJarPrefix(level));
			System.out.println(name + " (archive)");
			try {
				ZipInputStream entryZIS = new ZipInputStream(zis);
				listJar(entryZIS, ++level);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		} else if (name.endsWith("MANIFEST.MF")) {
			System.out.print(getEntryPrefix(level));
			System.out.print("+- ");
			System.out.print(name);
			Manifest mf = new Manifest(zis);
			Attributes main = mf.getMainAttributes();

			String cp = main.getValue(Attributes.Name.CLASS_PATH);
			if (cp != null) {
				System.out.print(" Class-Path: ");
				System.out.print(cp);
				System.out.print(' ');
			}
			System.out.println();
			return;
		}

		System.out.print(getEntryPrefix(level));
		System.out.print("+- ");
		System.out.println(name);
	}
}