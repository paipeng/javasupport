package ztool;

import static ztool.CliHelper.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Findjar extends CliBase {	

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help",         "Display this help page.").
		setOption("debug",        "Show extra debug info.").
		setOption("recurse",      "Search directory recursively.").
		setSummary(
			"Search in all jar files for entry specified.\n" +
			"").
		setUsage("Findjar [Options] <search_text> [dir ...]").
		setExamples(
			"  ztool Findjar MyClass\n" +
			"  ztool Findjar MyClass /my/project/lib /repository/lib\n" +
		"");
		
	}

	@Override
	public void run(Options opts) {
		List<File> files = new ArrayList<File>();
		List<String> args = opts.getArgs();
		String text = args.remove(0);
		if (args.size() == 0) {
			args.add(".");
		}
		for (String s : args) {
			File f = new File(s);
			if (f.isDirectory() && opts.has("recurse")) {
				files.addAll(getjarfiles(f));
			} else if (f.getName().endsWith(".jar")) {
				files.add(f);
			}
		}
		searchjar(Pattern.compile(text), files);
	}
	
	private List<File> getjarfiles(File dir) {
		List<File> result = new ArrayList<File>();
		File[] subfiles = dir.listFiles();
		for (File subfile : subfiles) {
			if (subfile.isDirectory()) {
				result.addAll(getjarfiles(subfile));
			} else if (subfile.getName().endsWith(".jar")) {
				//debug("Found jar: " + subfile);
				result.add(subfile);
			}
		}
		return result;
	}

	private void searchjar(Pattern pattern, List<File> files) {
		try {	
			for (File file : files) {
				JarFile jarFile = new JarFile(file);
				debug("Searching " + file);
				
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry elem = entries.nextElement();
					String name = elem.getName();
					debug("Processing " + name);
					if (pattern.matcher(name).find()) {
						println(file + ": " + name);
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
}
