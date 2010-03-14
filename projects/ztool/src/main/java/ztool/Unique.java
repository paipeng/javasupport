package ztool;

import static ztool.CliHelper.*;

import java.io.File;
import java.util.HashMap;

import ztool.CliBase;

public class Unique extends CliBase {
	
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	          "Display help page info.").
		setOption("showDuplicate",    "Show only duplicated lines.").
		setSummary(
			"Display unique, non duplicated rows from inputs.\n" +
			"").
		setUsage("ztool Unique [Options] [file ...]\n").
		setExamples(
			"  ztool Unique data.txt\n" +
			"  ztool Range 1:20 15:25 | ztool Unique\n" +
			"");
	}
	
	private boolean showDuplicate;
	
	@Override
	public void run(Options opts) {
		showDuplicate = opts.has("showDuplicate");
		
		final HashMap<String, Integer> dict = new HashMap<String, Integer>();
		if (opts.getArgsSize() == 0) {
			eachLine(System.in, new LineAction() {
				@Override
				public void onLine(String line) {
					run(dict, line);					
				}
			});
		} else {
			for (String name : opts.getArgs()) {
				eachLine(new File(name), new LineAction() {
					@Override
					public void onLine(String line) {
						run(dict, line);				
					}
				});		
			}
		}
	}
	
	private void run(HashMap<String, Integer> dict, String line) {
		Integer count = dict.get(line);
		if (count == null) {
			dict.put(line, 1);
			if (!showDuplicate) {
				println(line);
			}
		} else {
			if (showDuplicate) {
				int newCount = count + 1;
				dict.put(line, newCount);
				
				println(line);
				debug("  >> count: " + newCount);
			}
		}
	}
}
