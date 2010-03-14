package ztool;

import static ztool.CliHelper.*;

import java.io.File;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ztool.CliBase;

public class Find extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	          "Display help page info.").
		setOption("name=<regex>",     "Find only the filename matches this.").
		setOption("maxDepth=<regex>", "Set max directory depth to search.").
		setOption("noShowDir",        "Do not display directory name.").
		setSummary(
			"Find files in directories recursively. If no argument is given then\n" +
			"search the current directory. If no search name is given it will list\n" +
			"all the file name found.\n" +
			"").
		setUsage("ztool Find [Options] [dir ...]\n").
		setExamples(
			"  ztool Find\n" +
			"  ztool Find --name=\".*\"\n" +
			"  ztool Find src --name=\"java$\"\n" +
			"  ztool Find src otherProjectSrc --name=\"java$\"\n" +
			"");
	}
		
	private int maxDepth;
	private boolean noShowDir;
	
	@Override
	public void run(Options opts) {
		maxDepth = opts.getInt("maxDepth", -1);
		noShowDir = opts.has("noShowDir");
		
		String regex = opts.get("name", ".*");		
		Pattern pattern = Pattern.compile(regex);
		try {
			if (opts.getArgsSize() == 0) {
				find(new File("."), pattern, 0);
			} else {
				for (String name : opts.getArgs()) {
					File dir = new File(name);
					if (!dir.isDirectory()) {
						throw new RuntimeException("Argument " + dir + " is not an directory");
					}
					find(dir, pattern, 0);
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void find(File dir, Pattern pattern, int depth) throws IOException {
		File[] subFiles = dir.listFiles();
		if (subFiles == null) {
			return; 
		}
		
		for (File subFile : subFiles) {
			if (subFile.isDirectory()) {
				if (maxDepth == -1 || depth <= maxDepth) {
					find(subFile, pattern, depth + 1);
				}
			}
			
			if (noShowDir && subFile.isDirectory()) {
				continue;
			}
			
			String fileName = subFile.getPath();
			Matcher m = pattern.matcher(fileName);
			if (m.find()) {
				println(fileName);
			}
		}
	}
}
