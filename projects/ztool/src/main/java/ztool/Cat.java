package ztool;

import static ztool.CliHelper.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ztool.CliHelper.LineAction;

public class Cat extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
			setOption("help", 	          "Display help page info.").
			setOption("showPrefix", 	  "Display filename as output prefix.").
			setOption("tail=<numOfLines>", "Show only num_lines from the end of the file.").
			setOption("head=<numOfLines>", "Show only num_lines from the begining of the file.").
			setSummary(
				"ConCATenate text files or just to display portion of it (like\n" +
				"tail or head commands). If no argument is given, then will read\n" +
				"from STDIN, and threat each line as a filename for input." +
				"").
			setUsage("ztool Cat [Options] [file ...]\n").
			setExamples(
				"  ztool Cat data.txt\n" +
				"  ztool Cat *.txt\n" +
				"  ztool Cat data.txt --head=10 --showPrefix\n" +
				"  ztool Cat data.txt --tail=10 --showPrefix\n" +
				"");
	}
	
	private boolean showPrefix;
	private boolean tail;
	private boolean head;
	private int numOfTailLines;
	private int numOfHeadLines;
	
	@Override
	public void run(Options opts) {
		showPrefix = opts.has("showPrefix");
		tail = opts.has("tail");		
		
		if (tail) {
			if ("true".equals(opts.get("tail"))) {
				//When no value has given.
				numOfTailLines = 35;
			} else {
				numOfTailLines = opts.getInt("tail");
			}
			debug(">> numOfTailLines: " + numOfTailLines);
		}
		head = opts.has("head");
		if (head) {
			if ("true".equals(opts.get("head"))) {
				//When no value has given.
				numOfHeadLines = 35;
			} else {
				numOfHeadLines = opts.getInt("head");
			}
			debug(">> numOfHeadLines: " + numOfHeadLines);
		}
		
		//Take each line as filename from STDIN
		if (opts.getArgsSize() == 0) {
			debug("Processing from STDIN to read for filenames");
			eachLine(System.in, new LineAction() {			
				@Override
				public void onLine(String name) {
					cat(name);
				}
			});
		} else {
			for (String name : opts.getArgs()) {
				cat(name);
			}
		}
	}

	private void cat(String filename) {
		if (tail) {
			printTail(filename);
		} else if (head) {
			printHead(filename);
		} else {
			printAll(filename);
		}
	}

	private void printTail(final String filename) {
		final List<String> tailLines = new ArrayList<String>();
		eachLine(new File(filename), new LineAction() {
			@Override
			public void onLine(String line) {
				tailLines.add(line);
			}
		});

		int size = tailLines.size();	
		int start = size < numOfTailLines ? 0 : size - numOfTailLines;
		for (int i = start; i < size; i++) {
			String line = tailLines.get(i);
			if (showPrefix) {
				line = filename + " Line " + i + ": " + line;
			}					
			System.out.println(line);			
		}
	}

	private void printHead(final String filename) {
		withReader(new File(filename), new ReaderAction() {
			int lineNum = 0;			
			@Override
			public void onReader(BufferedReader reader) throws IOException {
				String line = null;
				while((line = reader.readLine()) != null) {
					lineNum++;
					if (showPrefix) {
						line = filename + " Line " + (lineNum) + ": " + line;
					}					
					System.out.println(line);
					
					if (lineNum >= numOfHeadLines) {
						break;
					}
				}
			}
		});
	}

	private void printAll(final String filename) {
		eachLine(new File(filename), new LineAction() {
			int lineNum = 0;
			@Override
			public void onLine(String line) {
				if (showPrefix) {
					line = filename + " Line " + (++lineNum) + ": " + line;
				}
				System.out.println(line);
			}
		});
	}
}
