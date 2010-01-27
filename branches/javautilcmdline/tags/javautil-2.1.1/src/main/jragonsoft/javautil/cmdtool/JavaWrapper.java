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
import java.util.Properties;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.util.DateUtils;
import jragonsoft.javautil.util.FileUtils;


/**
 * 
 * @author zemian
 * @version $Id: JavaWrapper.java 19 2006-04-27 15:45:49Z zdeng $
 */
public class JavaWrapper {
	public final static String RES_TEMPLATE = "/jragonsoft/template";

	public static Properties VAR_SUB_DICT = new Properties();

	public static void main(String[] args) {
		GetLongOpt opt = new GetLongOpt(args);
		if (opt.isOpt("help") || opt.isOpt("h") || opt.getArgsCount() != 2) {
			printExitHelp();
		}

		String type = opt.getOpt("type", "javautil");
		boolean noExt = opt.getBooleanOpt("noext", false);
		String destName = opt.getOpt("dest", ".");
		String wrapperName = opt.getArg(0);
		String mainClass = opt.getArg(1);
		File destFile = new File(destName);
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_FULL_MAIN_CLASS", mainClass);
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_YEAR", DateUtils
				.getTodayDate());

		System.out.println("Template Variable Substitutions: ");
		System.out.println(VAR_SUB_DICT);

		if (type.equals("javautil")) {
			makeTemplateFile(destFile, "run-javautil.sh", wrapperName);
			makeTemplateFile(destFile, "run-javautil.bat", wrapperName + ".bat");
		} else if (type.equals("dynamic")) {
			makeTemplateFile(destFile, "run.sh", wrapperName
					+ (noExt ? "" : ".sh"));
			makeTemplateFile(destFile, "run.bat", wrapperName + ".bat");
		} else {
			System.err.println("Unkown type: " + type);
			System.exit(1);
		}
	}

	private static File makeDir(File parent, String dir) {
		File ret = new File(parent, dir);
		System.out.println("makeDir: " + ret.getAbsolutePath());

		ret.mkdirs();
		return ret;
	}

	/** make a new file with same name as the template. */
	private static File makeTemplateFile(File parent, String templateFilename) {
		return makeTemplateFile(parent, templateFilename, null);
	}

	/** make a new file with based on a template. */
	private static File makeTemplateFile(File parent, String templateFilename,
			String newName) {
		try {
			File ret = new File(parent, newName == null ? templateFilename
					: newName);
			System.out.println("makeTemplateFile : " + ret.getAbsolutePath());

			FileOutputStream out = new FileOutputStream(ret);
			InputStream in = JavaWrapper.class.getResourceAsStream(RES_TEMPLATE
					+ "/" + templateFilename);
			if (templateFilename.endsWith(".sh"))
				FileUtils.copyFilteredStream(in, out, VAR_SUB_DICT, "\n");
			else
				FileUtils.copyFilteredStream(in, out, VAR_SUB_DICT);
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Description of the Method */
	public static void printExitHelp() {
		System.out
				.println("USAGE: JavaWrapper [options] WrapperName MainClass");
		System.out
				.println("  Using the wrapper template in this package to generate two wrapper files.");
		System.out.println("[options]");
		System.out.println("  --help         Help page");
		System.out.println("  --type=[javautil, dynamic]");
		System.out
				.println("                 Select a set of wrapper to create. javautil is default.");
		System.out
				.println("  --noext        Do not use extension for Unix wrapper.");
		System.out
				.println("  --dest=DIR     Change where to save the wrappers. Default current dir.");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out
				.println("  $Id: JavaWrapper.java 19 2006-04-27 15:45:49Z zdeng $");

		System.exit(1);
	}
}
