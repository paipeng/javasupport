/*
 *    2005 Zemian Deng
 * 
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 * 
 *        http://www.apache.org/licenses/LICENSE-2.0
 * 
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *                                                                               
 */

package jragonsoft.javautil.cmdtool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.DateUtils;
import jragonsoft.javautil.util.FileUtils;


/**
 * Javautil style of main program. See #printExitHelp() for more info.
 * 
 * @author zdeng
 */
public class MakeClassFile {
	public final static String RES_TEMPLATE = "/jragonsoft/template";

	/** Program options after it's been parsed. */
	private GetOpt opt;

	private String mainClassName = "Test";

	private String packageName = null;

	/** Instantiate main program and parse args then run. */
	public static void main(String[] args) {
		MakeClassFile main = new MakeClassFile();
		main.opt = new GetLongOpt(args);
		main.run();
	}

	/** Run the main program logic. */
	public void run() {
		if (opt.getArgsCount() < 1 || opt.isOpt("h") || opt.isOpt("help"))
			printExitHelp();

		for (int i = 0, max = opt.getArgsCount(); i < max; i++) {
			mainClassName = opt.getArg(i);
			//remove java extension if it's given
			if (mainClassName.lastIndexOf(".java") != -1)
				mainClassName = mainClassName.substring(0, mainClassName
						.lastIndexOf(".java"));
			packageName = opt.getOpt("package", null);

			File destDir = new File(".");
			if (packageName != null) {
				destDir = new File(destDir, packageName.replaceAll("\\.", "/"));
				destDir.mkdirs();
			}
			Properties dict = new Properties();
			dict.setProperty("__RES_TEMPLATE_MAIN_CLASS", mainClassName);
			dict.setProperty("__RES_TEMPLATE_YEAR", DateUtils.getTodayDate());
			dict
					.setProperty("__RES_TEMPLATE_PACKAGE_DECLARE",
							(packageName == null ? "" : "package "
									+ packageName + ";"));
			dict.setProperty("__RES_TEMPLATE_MAIN_CLASS", mainClassName);
			dict.setProperty("__RES_TEMPLATE_FULL_MAIN_CLASS",
					(packageName == null ? mainClassName : packageName + "."
							+ mainClassName));

			String type = opt.getOpt("type", "plain");
			if (type.equals("plain"))
				makeTemplateFile(dict, destDir, "Main.java.template",
						mainClassName + ".java");
			else if (type.equals("option"))
				makeTemplateFile(dict, destDir, "OptionMain.java.template",
						mainClassName + ".java");
			else if (type.equals("javautilOption"))
				makeTemplateFile(dict, destDir,
						"JavautilOptionMain.java.template", mainClassName
								+ ".java");
			else
				System.err.println("Wrong type: " + type);
		}
	}

	/** Print helppage then exit program. */
	public void printExitHelp() {
		System.out.println("USAGE: MakeClassFile [options] <ClassName>");
		System.out.println("[options]");
		System.out.println("  -h, --help      Display help page.");
		System.out.println("  --package=NAME  Specify package name.");
		System.out.println("  --type=[plain, option, javautilOption]");
		System.out
				.println("                  Specify main class type. Default plain.");
		System.out.println("JavaUtil By Zemian Deng.");

		System.exit(0);
	}

	public void makeEmptyMain(String mainClassName) {
		File destDir = new File(".");
		if (packageName != null) {
			destDir = new File(destDir, packageName.replaceAll("\\.", "/"));
			destDir.mkdirs();
		}
		Properties dict = new Properties();
		dict.setProperty("__RES_TEMPLATE_MAIN_CLASS", mainClassName);
		dict.setProperty("__RES_TEMPLATE_YEAR", DateUtils.getTodayDate());
		dict.setProperty("__RES_TEMPLATE_PACKAGE_DECLARE",
				(packageName == null ? "" : "package " + packageName + ";"));
		dict.setProperty("__RES_TEMPLATE_MAIN_CLASS", mainClassName);
		dict.setProperty("__RES_TEMPLATE_FULL_MAIN_CLASS",
				(packageName == null ? mainClassName : packageName + "."
						+ mainClassName));
		makeTemplateFile(dict, destDir, "Main.java.template", mainClassName
				+ ".java");
	}

	/** make a new file with based on a template. */
	private File makeTemplateFile(Properties dict, File parent,
			String templateFilename, String newName) {
		try {
			File ret = new File(parent, newName == null ? templateFilename
					: newName);
			System.out.println("makeTemplateFile : " + ret.getAbsolutePath());

			FileOutputStream out = new FileOutputStream(ret);
			InputStream in = JavaWrapper.class.getResourceAsStream(RES_TEMPLATE
					+ "/" + templateFilename);
			if (templateFilename.endsWith(".sh"))
				FileUtils.copyFilteredStream(in, out, dict, "\n");
			else
				FileUtils.copyFilteredStream(in, out, dict);
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
