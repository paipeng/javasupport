package ztool;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Which extends CliBase {

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	          "Display help page info.").
		setSummary(
			"Which PATH that contains the given command.\n" +
			"").
		setUsage("ztool Which [Options] <commandName>\n").
		setExamples(
			"  ztool Which java\n" +
			"  ztool Which notepad\n" +
			"");
	}

	@Override
	public void run(Options opts) {
		String command = opts.getArg(0);
		if (System.getProperty("os.name").toUpperCase().startsWith("WINDOWS")) {
			command += "\\.(exe|com|bat)";
		}
		
		Map<String, String> env = System.getenv();
		String pathKey = null;
		for(String k : env.keySet()) {
			if (k.matches("(?i)PATH") ) {
				pathKey = k;
				break;
			}
		}
		if (pathKey == null) {
			throw new RuntimeException("No PATH env found.");
		}
		
		String[] paths = env.get(pathKey).split(
				System.getProperty("path.separator"));
		
		for (String p : paths) {
			debug("Searching " + p);
			File dir = new File(p);
			if (!dir.isDirectory()) {
				debug("WARN: " + dir + " is not a directory.");
				continue;
			}
			
			File[] files = dir.listFiles();
			if (files != null) {
				List<File> sortFiles = Arrays.asList(files);
				Collections.sort(sortFiles);
				for (File f : sortFiles) {
					if (f.getName().matches("(?i)" + command)) {
						System.out.println(f.getPath());
					}
				}
			} else {
				debug("WARN: dir.listFiles return null!");
			}
		}
	}

}
