package ztool;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ztool.CliHelper.*;

public class Cut extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("splitSep=<regex>", "Define custom split seperator.").
		setOption("joinSep=<regex>",  "Define custom join seperator when printing.").
		setOption("inputFile=<file>", "Specify input file.").
		setOption("reverseCut",       "Keep the cut column and remove others.").
		setSummary(
			"Split each input line and cut out the zero based column specified.\n" +
			"If no inputFile is given then STDIN is used.\n" +
			"\n" +
			"You may use negative indexing on cutting column. When using negative\n" +
			"indexes, make sure you use \"--\" after thec command so it will not\n" +
			"parse as option flag.\n" +
			"").
		setUsage("ztool Cut [Options] [cutColumnIdx ...]\n").
		setExamples(
			"  ztool Cut 2 --inputFile=data.txt\n" +
			"  ztool Printf \"a b c\\nd e f\" | ztool Cut 2\n" +
			"  ztool Printf \"a b c\\nd e f\" | ztool Cut -- -1\n" +
			"  ztool Printf \"a b c\\nd e f\" | ztool Cut -r -- -1\n" +
			"  ztool Printf \"a|b|c\\nd|e|f\" | ztool Cut --splitSep=\"\\|\"\n" +
			"  ztool Printf \"a|b|c\\nd|e|f\" | ztool Cut --splitSep=\"\\|\" 1\n" +
			"");
	}
	
	private String splitSep;
	private String joinSep;
	private File inputFile;
	private boolean reverseCut;
	
	@Override
	public void run(Options opts) {
		splitSep = opts.get("splitSep", "\\s+");
		joinSep = opts.get("joinSep", "\t");
		inputFile = opts.getFile("inputFile", null);
		reverseCut = opts.has("reverseCut");
		
		final Set<Integer> cutIndexes = new HashSet<Integer>();
		for (String arg : opts.getArgs()) {
			cutIndexes.add(Integer.parseInt(arg));
		}
		debug(">> cutIndexes " + cutIndexes);
		
		InputStream ins = getInputStream(inputFile, System.in);		
		eachLine(ins, new LineAction() {			
			@Override
			public void onLine(String line) {
				List<String> result = new ArrayList<String>();
				String[] words = line.split(splitSep);
				
				int wordLen = words.length;
				debug(">> wordLen " + wordLen);
				for (int i = 0; i < wordLen; i++) {
					String word = words[i];
					debug(">> Checking column " + i + ", word=" + word);
					
					boolean keep = !cutIndexes.contains(i);
					debug("   keep? " + keep);
					
					if (keep) {
						//Try negative index search
						keep = !cutIndexes.contains(i - wordLen);
						debug("   negative index keep? " + keep);
					}
					
					if (reverseCut) {
						keep = !keep;
						debug("   reversing keep value!");
					}
										
					
					if (keep) {
						result.add(word);
					}
				} 
				
				String first = result.remove(0);
				print(first);
				
				for(String word : result) {
					print(joinSep);
					print(word);
				}
				println("");
			}
		});
	}

}
