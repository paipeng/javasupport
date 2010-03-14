package ztool;

import static ztool.CliHelper.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Sort extends CliBase {	
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	                  "Display help page info.").
		setOption("shuffle",                  "Shuffle inputs randomly instead of sort.").
		setOption("type=<string|int|double>", "Specify the input data type. Default string.").
		setSummary(
			"Sort inputs data.\n" +
			"").
		setUsage("ztool Sort [Options] [argument ...]\n").
		setExamples(
			"  ztool Range 1:20 | ztool Sort --shuffle\n" +
			"  ztool Range 1:20 | ztool Sort --shuffle | ztool Sort --type=int\n" +
			"");
	}
		
	private String type;
	private boolean shuffle;
	
	@Override
	public void run(Options opts) {
		type = opts.get("type", "string");
		shuffle = opts.has("shuffle");
		
		final List<String> args = new ArrayList<String>();
		if (opts.getArgsSize() == 0) {
			eachLine(System.in, new LineAction() {			
				@Override
				public void onLine(String line) {
					if (!line.trim().equals("")) {
						args.add(line);
					}
				}
			});
		} else {
			args.addAll(opts.getArgs());
		}
		
		if (type.equals("string")) { 
			processStringList(args);
		} else if (type.equals("int")) { 
			processIntList(args);
		} else if (type.equals("double")) { 
			processDoubleList(args);
		} else {
			throw new RuntimeException("Invalid data type: " + type);
		}
	}

	private void processStringList(List<String> list) {
		if (shuffle) {
			Collections.shuffle(list);
		} else {
			Collections.sort(list);
		}
		
		for (String v : list) {
			println(v);
		}		
	}

	private void processDoubleList(List<String> args) {
		List<Double> list = new ArrayList<Double>();
		for (String arg : args) {
			list.add(Double.parseDouble(arg));
		}
		
		if (shuffle) {
			Collections.shuffle(list);
		} else {
			Collections.sort(list);
		}
		
		for (double v : list) {
			println(String.valueOf(v));
		}		
	}

	private void processIntList(List<String> args) {
		List<Integer> list = new ArrayList<Integer>();
		for (String arg : args) {
			list.add(Integer.parseInt(arg));
		}
		
		if (shuffle) {
			Collections.shuffle(list);
		} else {
			Collections.sort(list);
		}
		
		for (int v : list) {
			println(String.valueOf(v));
		}		
	}
}
