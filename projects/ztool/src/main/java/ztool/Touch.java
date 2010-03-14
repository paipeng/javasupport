package ztool;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Touch extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser
		.setSummary("Touch and set last modified datetime to current time of given files.\n" +
				"If the file does not exist, create an empty file.")
		.setUsage("ztool Touch [Options] [file ...]\n")
		.setExamples("  ztool Touch myfile.txt\n");
	}

	@Override
	public void run(Options opts) {
		long ts = System.currentTimeMillis();
		for (String arg : opts.getArgs()) {
			File file = new File(arg);
			touch(file, ts);
		}
	}
    public static void touch(File file, long time) {
        if (file.isDirectory()) {
        	for (File childFile:file.listFiles()) {
        		touch(childFile, time);
        	}
        } else {
        	if (!file.exists()) {
        		CliHelper.withWriter(file, new CliHelper.WriterAction() {					
					@Override
					public void onWriter(PrintWriter writer) throws IOException {
						writer.write("");
					}
				});
        	} else {
        		file.setLastModified(time);
        	}
        }
    }
}
