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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.Timer;
import java.util.TimerTask;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.FileUtils;


public class TailFile {
	public static void main(String[] args) {
		GetOpt opt = new GetOpt(args);
		if (opt.isOpt("h")) {
			printExitHelp();
		}

		if (opt.getArgsCount() != 1) {
			System.err.println("Please supply a filename.");
			System.exit(-1);
		}

		String fn = opt.getArg(0);
		File inputFile = new File(fn);
		long checkInterval = opt.getLongOpt("i", 500);
		boolean forceTail = opt.isOpt("f");
		int startLastLine = opt.getIntOpt("n", 200);

		printTail(inputFile, startLastLine);
		if (forceTail) {
			Timer fileChecker = new Timer();
			fileChecker.schedule(new PrintLastFileChanged(inputFile), 0,
					checkInterval);

			//The main thread will response to Enter key for empty line
			//Scanner sc = new Scanner(System.in);
			//while (sc.hasNext()) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String line = null;
			try {
				while ((line = in.readLine()) != null) {
					System.out.println();
				}
			} catch (IOException e) {
				//throw new RuntimeException(e);
			}
		}
	}

	public static void printTail(File file, int startLastLine) {
		String lines[] = FileUtils.getLines(file);
		int start = 0;
		if (lines.length > startLastLine)
			start = lines.length - startLastLine;
		for (int i = start; i < lines.length; i++) {
			System.out.println(lines[i]);
		}
	}

	public static class PrintLastFileChanged extends TimerTask {
		long lastMod;

		long lastLength;

		File file;

		public PrintLastFileChanged(File file) {
			this.file = file;
			lastMod = file.lastModified(); //these two will prevent print out
										   // of existing file
			lastLength = FileUtils.getLength(file);
		}

		public void printLastModLenFile() throws IOException,
				FileNotFoundException {
			RandomAccessFile raf = new RandomAccessFile(file, "r");
			raf.seek(lastLength);
			String line = null;
			while ((line = raf.readLine()) != null) {
				System.out.println(line);
			}
			lastMod = file.lastModified();
			lastLength = raf.length();
		}

		public void run() {
			if (file.lastModified() > lastMod) {
				try {
					printLastModLenFile();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
	}

	public static void printExitHelp() {
		System.out.println("USAGE: TailFile [options] FILE");
		System.out.println("  Print the tail content of a file.");
		System.out.println("[options]");
		System.out.println("  -h         Help page");
		System.out
				.println("  -nNUM      Number of lines to print counting from end up. Default 200.");
		System.out
				.println("  -f         Keep tail running to detect file changes");
		System.out
				.println("  -iMSEC     Specify the interval to check file when -f is on. Default 500.");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out
				.println("  $Id: TailFile.java 4 2006-03-16 15:27:19Z zemian $");

		System.exit(1);
	}
}
