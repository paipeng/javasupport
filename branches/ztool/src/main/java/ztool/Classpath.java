package ztool;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Classpath extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	          "Display help page info.").
		setOption("fullpath",         "Use full path instead of relative path.").
		setOption("recurse",          "Search dir recursively.").
		setOption("printOnly",        "Print classpath string with each entry per new line.").
		setSummary(
			"Find all jar files in a dir and build a Java Classpath string.\n" +
			"").
		setUsage("ztool Classpath [Options] [dir ...]\n").
		setExamples(
			"  ztool Classpath data.txt\n" +
			"");
	}

	public final static String PATH_SEP = System.getProperty("path.separator");
	private JarFilenameFilter jarFilenameFilter = new JarFilenameFilter();
	private DirFileFilter dirFilter = new DirFileFilter();
	private boolean fullpath;
	private boolean recurse;
	private boolean printOnly;
	
	@Override
	public void run(Options opts) {		
		try {
			fullpath = opts.has("fullpath");
			recurse = opts.has("recurse");
			printOnly = opts.has("printOnly");
					
			StringBuilder classpath = new StringBuilder();
			if (opts.getArgsSize() == 0) {
				classpath.append(System.getProperty("java.class.path"));
			} else {
				for (String name : opts.getArgs()) {
					File dir = new File(name);
					String cp = buildClasspath(dir);				
					if (cp.equals("")) {
						classpath.append(fullpath ? dir.getCanonicalPath() : dir.getPath());
					}
					classpath.append(cp);
					classpath.append(PATH_SEP);
				}
			}
			
			// Get rid off the last extra path sep.
			String cp = classpath.substring(0, classpath.length() - 1);
			displayClasspath(cp);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private void displayClasspath(String classpath) {
		if (printOnly) {
			String[] paths = classpath.split(PATH_SEP);
			
			for (String p : paths) {
				System.out.println(p);
			}
		} else {
			System.out.println(classpath);
		}
	}

	private String buildClasspath(File dir) throws Exception {
		StringBuilder classpath = new StringBuilder();
		if (dir.isDirectory()) {
			File[] subfiles = dir.listFiles(jarFilenameFilter);
			for (File subfile : subfiles) {
				classpath.append(fullpath ? subfile.getCanonicalPath() : subfile.getPath());
				classpath.append(PATH_SEP);
			}
			if (recurse) {
				File[] subdirs = dir.listFiles(dirFilter);
				for (File subdir : subdirs) {
					classpath.append(buildClasspath(subdir));
					classpath.append(PATH_SEP);
				}
			}
		}
		
		String cp = null;
		if (classpath.length() > 0) {
			cp = classpath.substring(0, classpath.length() - 1);
		} else {
			cp = "";
		}
		return cp;
	}
	
	private static class JarFilenameFilter implements FilenameFilter {
		@Override
		public boolean accept(File dir, String name) {
			return name.endsWith(".jar");
		}		
	}
	private static class DirFileFilter implements FileFilter {

		@Override
		public boolean accept(File pathname) {
			return pathname.isDirectory();
		}
			
	}
}
