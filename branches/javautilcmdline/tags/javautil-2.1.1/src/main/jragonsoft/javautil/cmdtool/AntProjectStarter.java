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
 * @version $Id: AntProjectStarter.java 19 2006-04-27 15:45:49Z zdeng $
 */
public class AntProjectStarter {
	public final static String RES_TEMPLATE = "/jragonsoft/template";

	public static Properties VAR_SUB_DICT = new Properties();

	public static String packagePath;

	public static String packageName;

	public static void main(String[] args) {
		GetLongOpt opt = new GetLongOpt(args);
		if (opt.isOpt("help") || opt.isOpt("h")) {
			printExitHelp();
		}

		if (opt.getArgsCount() != 1) {
			System.err.println("Please supply a projectName.");
			System.exit(-1);
		}

		String projName = opt.getArg(0);
		String dest = opt.getOpt("dest", ".");
		String type = opt.getOpt("type", "app");
		String mainClassName = "Main";

		packageName = opt.getOpt("package", null);
		if (packageName != null)
			packagePath = packageName.replaceAll("\\.", "/");

		File destFile = new File(dest);
		if (!destFile.exists()) {
			System.err.println("Destination dir doesn't exist!");
			System.exit(-2);
		}

		File projDir = new File(destFile, projName);
		/*
		 * if(projDir.exists()){ System.err.println("Project name already
		 * exists! Please remove it first."); System.exit(-3); }
		 */
		projDir.mkdir();

		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_YEAR", DateUtils
				.getTodayDate());
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_PROJECT_NAME", projName);
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_PROJECT_PATH", projDir
				.getAbsolutePath());
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_PACKAGE_DECLARE",
				(packageName == null ? "" : "package " + packageName + ";"));
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_MAIN_CLASS", mainClassName);
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_FULL_MAIN_CLASS",
				(packageName == null ? mainClassName : packageName + "."
						+ mainClassName));

		if (type.equalsIgnoreCase("basic"))
			buildBasicProject(projDir);
		else if (type.equalsIgnoreCase("app"))
			buildAppProject(projDir);
		else if (type.equalsIgnoreCase("svnapp"))
			buildSvnProject(projDir);
		else if (type.equalsIgnoreCase("webapp"))
			buildTomcatWebappProject(projDir);
		else if (type.equalsIgnoreCase("webapp5.0"))
			buildTomcat50WebappProject(projDir);
		else
			System.err.println("Unknown project type to build: " + type);
	}

	public static void buildBasicProject(File projDir) {
		makeFile(projDir, "build-basic.xml", "build.xml");
		File src = makeDir(projDir, "src"
				+ (packagePath == null ? "" : "/" + packagePath));
		makeFile(src, "OptionMain.java.template", "Main.java");
	}

	public static void buildAppProject(File projDir) {
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_ANT_IMPORT", "build-app.xml");
		makeFile(projDir, "build-app.xml");
		makeFile(projDir, "build-importing.xml", "build.xml");
		buildProject(projDir);
	}

	public static void buildSvnProject(File projDir) {
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_ANT_IMPORT",
				"build-svnapp.xml");
		makeFile(projDir, "build-app.xml");
		makeFile(projDir, "build-svnapp.xml");
		makeFile(projDir, "build-importing.xml", "build.xml");
		makeFile(projDir, "project-template.properties", "project.properties");
		buildProject(projDir);
	}

	public static void buildTomcatWebappProject(File projDir) {
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_ANT_IMPORT",
				"build-tomcatwebapp.xml");
		makeFile(projDir, "tomcat-build-template.properties",
				"build.properties");

		makeFile(projDir, "build-app.xml");
		makeFile(projDir, "build-svnapp.xml");
		makeFile(projDir, "build-tomcatwebapp.xml");
		makeFile(projDir, "build-importing.xml", "build.xml");
		makeFile(projDir, "project-template.properties", "project.properties");
		makeFile(projDir, "tomcat-webapp-context-template.xml",	"context-template.xml");
		buildProject(projDir, false);

		File web = makeDir(projDir, "web");
		File indexJSP = makeFile(web, "index-template.jsp", "index.jsp");
		
		File webINF = makeDir(web, "WEB-INF");
		File lib = makeDir(webINF, "lib");
		File webXML = makeFile(webINF, "web-template.xml", "web.xml");
	}

	public static void buildTomcat50WebappProject(File projDir) {
		VAR_SUB_DICT.setProperty("__RES_TEMPLATE_ANT_IMPORT",
				"build-tomcat5.0webapp.xml");
		makeFile(projDir, "tomcat5.0-build-template.properties",
				"build.properties");
		makeFile(projDir, "tomcat5.0-webapp-context-template.xml",
				"context.xml");
		makeFile(projDir, "tomcat-webapp-context-template.xml", "context.xml");
		makeFile(projDir, "build-app.xml");
		makeFile(projDir, "build-svnapp.xml");
		makeFile(projDir, "build-tomcatwebapp.xml", "build-tomcatwebapp.xml");
		makeFile(projDir, "build-tomcat5.0webapp.xml", "build-tomcat5.0webapp.xml");
		makeFile(projDir, "build-importing.xml", "build.xml");
		makeFile(projDir, "project-template.properties", "project.properties");
		buildProject(projDir, false);

		File web = makeDir(projDir, "web");
		File indexJSP = makeFile(web, "index-template.jsp", "index.jsp");

		File webINF = makeDir(web, "WEB-INF");
		File lib = makeDir(webINF, "lib");
		File webXML = makeFile(webINF, "web-template.xml", "web.xml");
	}

	public static void buildProject(File projDir) {
		buildProject(projDir, true);
	}

	public static void buildProject(File projDir, boolean mkLib) {
		File bin = makeDir(projDir, "bin");
		makeFile(bin, "run.sh");
		makeFile(bin, "run.bat");
		File doc = makeDir(projDir, "doc");
		makeFile(doc, "Readme.txt");
		makeFile(doc, "Release.txt");

		if (mkLib)
			makeDir(projDir, "lib");

		File src = makeDir(projDir, "src");
		File main = makeDir(src, "main"
				+ (packagePath == null ? "" : "/" + packagePath));
		makeFile(main, "OptionMain.java.template", "Main.java");

		File res = makeDir(src, "resource");
		makeFile(res, "log4j-template.properties", "log4j.properties");

		File test = makeDir(src, "test/unittest"
				+ (packagePath == null ? "" : "/" + packagePath));
		makeFile(test, "OptionMainTest.java.template", "MainTest.java");
	}

	private static File makeDir(File parent, String dir) {
		File ret = new File(parent, dir);
		System.out.println("makeDir: " + ret.getAbsolutePath());

		ret.mkdirs();
		return ret;
	}

	private static File makeFile(File parent, String filename) {
		return makeFile(parent, filename, null);
	}

	private static File makeFile(File parent, String filename, String newName) {
		try {
			File ret = new File(parent, newName == null ? filename : newName);
			System.out.println("makeFile : " + ret.getAbsolutePath());

			FileOutputStream out = new FileOutputStream(ret);
			if (packageName != null && filename.endsWith(".java"))
				out.write(("package " + packageName + ";\n").getBytes());

			InputStream in = AntProjectStarter.class
					.getResourceAsStream(RES_TEMPLATE + "/" + filename);
			if (in == null)
				throw new RuntimeException("File resource not found! "
						+ RES_TEMPLATE + "/" + filename);

			if (filename.endsWith(".sh"))
				FileUtils.copyFilteredStream(in, out, VAR_SUB_DICT, "\n");
			else if (filename.endsWith(".bat"))
				FileUtils.copyFilteredStream(in, out, VAR_SUB_DICT, "\r\n");
			else
				FileUtils.copyFilteredStream(in, out, VAR_SUB_DICT);
			return ret;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void printExitHelp() {
		System.out.println("USAGE: AntProjectStarter [options] ProjectName");
		System.out
				.println("  Create an ant project structure that includes build.xml file.");
		System.out.println("[options]");
		System.out.println("  --help         Help page");
		System.out
				.println("  --package=NAME Add appropriate directories for this package. Default empty");
		System.out
				.println("  --dest=DIR     Change the path where to save project. Default current dir.");
		System.out
				.println("  --type=BUILD   Create different file set structure, where BUILD can be:");
		System.out
				.println("                   app        - build a small standalone application. (Default)");
		System.out
				.println("                   svnapp     - build a large application with Subversion support.");
		System.out
				.println("                   webapp     - build a Tomcat's web application project.");
		System.out
				.println("                   webapp5.0  - build a Tomcat's web application project.");
		System.out
				.println("                   basic      - build a tiny project build.xml.");
		System.out.println("CREDITS:");
		System.out.println("  ZMan Java Utility. <zemiandeng@gmail.com>");
		System.out
				.println("  $Id: AntProjectStarter.java 19 2006-04-27 15:45:49Z zdeng $");

		System.exit(1);
	}
}
