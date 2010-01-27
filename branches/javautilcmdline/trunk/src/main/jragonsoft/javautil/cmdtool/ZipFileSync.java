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
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.util.FileUtils;


public class ZipFileSync {
	public static void main(String[] args) throws Exception {
		GetLongOpt opt = new GetLongOpt(args);
		if (opt.isOpt("help") || opt.isOpt("h")) {
			printExitHelp();
		}

		if (opt.getArgsCount() != 2) {
			System.err.println("Please supply a DEST and SRC.");
			System.exit(-1);
		}

		File destDir = new File(opt.getArg(0));
		String srcName = opt.getArg(1);
		File srcFile = null;
		try {
			File download = new File(System.getProperty("user.home")
					+ "/download");
			if (!download.exists())
				download.mkdirs();
			srcFile = WebGet.webget(srcName, download.getAbsolutePath());
		} catch (Exception e) {
			//throw new RuntimeException(e);
			srcFile = new File(srcName);
		}

		ZipFile zipFile = new ZipFile(srcFile);
		for (Enumeration e = zipFile.entries(); e.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) e.nextElement();
			File localFile = new File(destDir, entry.getName());
			if (localFile.lastModified() < entry.getTime()) {
				if (entry.isDirectory() && !localFile.exists()) {
					System.out.println("dir " + entry.getName()
							+ " is out of sync, creating...");
					localFile.mkdirs();
					continue;
				}
				System.out.println(entry.getName() + " is out of sync");
				InputStream in = zipFile.getInputStream(entry);
				FileOutputStream fout = new FileOutputStream(localFile);
				FileUtils.copyStream(in, fout);
				System.out.println("  replaced");
			} else {
				System.out.println("Local is update to date. No change.");
			}
		}
	}

	/** Description of the Method */
	static void printExitHelp() {
		System.out
				.println("USAGE: ZipFileSync [options] DEST_DIR SRC_ZIPFILE_URL");
		System.out
				.println("  Synchronize a zipfile(can be remote) to a local expanded dir based on last");
		System.out.println("  modified timestamp date.");
		System.out.println("[options]");
		System.out.println("  --help           Help page");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out
				.println("  $Id: ZipFileSync.java 19 2006-04-27 15:45:49Z zdeng $");

		System.exit(1);
	}
}