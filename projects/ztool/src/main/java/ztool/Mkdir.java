package ztool;

import java.io.File;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Mkdir extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser
				.setSummary("Make a new directory on your file system.\n" +
						"This tool will work with dot prefix directories on Windows!\n")
				.setUsage("ztool Mkdir [Options] [dir ...]\n")
				.setExamples(
					  "  ztool Mkdir mydir\n"
					+ "  ztool Mkdir .ant/lib .groovy/lib\n"
					+ "");
	}

	@Override
	public void run(Options opts) {
		for (String arg : opts.getArgs()) {
			File dir = new File(arg);
			System.out.println("Making dir: " + dir);
			if (!dir.mkdirs()) {
				throw new RuntimeException("Failed to make directory " + dir);
			}
		}
	}
}
