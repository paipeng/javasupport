package ztool;

import static ztool.CliHelper.eachLine;

import java.io.File;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ztool.CliHelper.LineAction;
import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Grep extends CliBase {

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help",                "Display this help page.").
		setOption("debug",               "Show extra debug info.").
		setOption("recursive",           "Walk directory recursively.").
		setOption("name=<regex>",        "Search only these matched file names when walk dir.").
		setOption("showGroup=<indexes>", "If regex is used, display the matched group only.").
		setOption("regex",               "Use Java regular expression as search_text.").
		setOption("showPrefix",          "Print filename or line number as prefix to result.").
		setOption("trim",                "Trim found result before displaying.").
		setOption("joinSep",             "Use this sep if showGroup has more than two indexes.").
		setSummary("Grep for search_text in one or more files. If no file are given, then use STDIN.\n").
		setUsage("ztool Grep [Options] <search_text> [file ...]").
		setExamples(
			"  ztool Grep private *.java\n" +
			"  ztool Grep --regex \"private (void|String)\" *.java\n" +
			"  ztool Grep --regex --noPrefix --showGroup=1 \"private (void|String)\" *.java\n" +
			"  ztool Grep --regex \"void (set[A-Z]\\w+)\" *.java\n" +
			"");
	}

	private boolean regex = false;
	private boolean showPrefix = false;
	private boolean trim = false;
	private List<Integer> showGroup;
	private Greper greper;
	private String joinSep;
	private boolean recursive = false;
	private String name;

	@Override
	public void run(Options opts) {
		recursive = opts.has("recursive");
		regex = opts.has("regex");
		showPrefix = opts.has("showPrefix");
		trim = opts.has("trim");
		joinSep = opts.get("joinSep", "\t");
		name = opts.get("name", ".*");
		
		//If showGrup=0, then show all groups
		showGroup = Range.IntRangeList.makeFrom(opts.get("showGroup", "0")).toList();
		

		String search = opts.getArg(0);
		greper = regex ? new RegexFindGrep(search) : new SimpleGrep(search);
		

		if (opts.getArgsSize() == 1) {
			debug("Grepping from STDIN.");
			LineAction lineAction = new GrepLineAction("Line ");
			eachLine(System.in, lineAction);
		} if (opts.getArgsSize() == 2) {
			String fn = opts.getArg(1);
			debug("Grepping from :" + fn);
			LineAction lineAction = new GrepLineAction("Line ");
			
			File file = new File(fn);
			if (file.isDirectory()) {
				grepDir(file, Pattern.compile(name));
			} else {
				eachLine(file, lineAction);
			}
		} else {
			//Process each file from argument
			for (int i = 1; i < opts.getArgsSize(); i++) {
				String fn = opts.getArg(i);
				LineAction lineAction = new GrepLineAction(fn + " Line ");
				debug("Grepping from file: " + fn);

				File file = new File(fn);
				if (file.isDirectory()) {
					grepDir(file, Pattern.compile(name));
				} else {
					eachLine(file, lineAction);
				}
			}
		}
	}
		
	private void grepDir(File dir, Pattern namePattern) {
		File[] subFiles = dir.listFiles();
		if (subFiles == null) {
			return;
		}
		for (File file : subFiles) {
			if (file.isDirectory() && recursive) {
				grepDir(file, namePattern);
			} else {
				Matcher m = namePattern.matcher(file.getPath());
				if (m.find()) {
					LineAction lineAction = new GrepLineAction(file.getPath() + " Line ");
					eachLine(file, lineAction);
				}
			}
		}
	}

	private class GrepLineAction implements LineAction {
		private String defaultPrefix = "";
		public GrepLineAction(String defaultPrefix) {
			this.defaultPrefix = defaultPrefix;
		}
		int lineCount = 0;				
		@Override
		public void onLine(String line) {
			lineCount++;
			String resultLine = greper.processGrep(line);
			if (resultLine != null) {
				String prefix = showPrefix ? (defaultPrefix + lineCount + ": ") : "" ;
				System.out.print(prefix);
				String outLine = trim ? resultLine.trim() : resultLine;
				System.out.println(outLine);
			}
		}
	}
	
	private interface Greper {
		public String processGrep(String input);
	}
	
	private class RegexFindGrep implements Greper {
		private Pattern pattern = null;

		public RegexFindGrep(String search){
			pattern = Pattern.compile(search);
		}
		
		@Override
		public String processGrep(String input) {
			Matcher matcher = pattern.matcher(input);
			boolean found = matcher.find();
			if (!found) {
				return null;
			} else {
				if (showGroup.size() == 1 && showGroup.get(0) == 0) {
					StringBuilder sb = new StringBuilder();
					int len = matcher.groupCount();
					for (int i = 1; i <= len; i++) {
						sb.append(matcher.group(i) + joinSep);
					}
					return sb.toString();
				} else if (showGroup.size() > 0) {
					StringBuilder sb = new StringBuilder();
					if (showGroup.size() == 1) {
						sb.append(matcher.group(showGroup.get(0)));
					} else {
						for (int i : showGroup) {
							sb.append(matcher.group(i) + joinSep);
						}
					}
					return sb.toString();
				} else {
					return input;
				}
			}
		}		
	}
	private class SimpleGrep implements Greper {
		private String search = null;
		public SimpleGrep(String search) {
			this.search = search;
		}
		@Override
		public String processGrep(String input) {
			if (!input.contains(search)) {
				return null;
			} else {
				return input;
			}
		}		
	}
}
