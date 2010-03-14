package ztool;

import static ztool.CliHelper.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ztool.CliHelper.LineAction;
import ztool.CliHelper.Options;

public class Count extends CliBase {
	
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	           "Display help page info").
		setOption("sum", 	           "Sum up all the input numbers").
		setOption("avg", 	           "Average up all the input numbers").
		setOption("max", 	           "Find the max from all input numbers").
		setOption("min", 	           "Find the max from all input numbers").
		setOption("countStdinAsFiles", "Count line from each filename read from STDIN.").
		setSummary(
			"Count number of lines from input.\n" +
			"").
		setUsage("ztool Count [Options] [file ...]\n").
		setExamples(
			"  ztool Count data.txt\n" +
			"  ztool Sysinfo -s | ztool Count\n" +
			"  ztool Count *.java | ztool Cut 0 -r | ztool Count --sum\n" +
			"  ztool Find src -name=\"java$\" --noShowDir | ztool Count --countStdinAsFiles | ztool Cut 0 -r | ztool Count --sum\n" +				
			"");
	}
			
	@Override
	public void run(Options opts) {
		if (opts.has("countStdinAsFiles")) {
			eachLine(System.in, new LineAction() {				
				@Override
				public void onLine(String name) {
					try {
						count(new FileInputStream(name), name, true);
					} catch (FileNotFoundException e) {
						throw new RuntimeException("File not found " + name);
					}
				}
			});
			return; //We are done, let's exit.
		}
		
		if (opts.has("sum")) {
			sumTotal(opts);
		} else if (opts.has("avg")) {
			avgTotal(opts);
		} else if (opts.has("max")) {
			max(opts);
		} else if (opts.has("min")) {
			min(opts);
		} else {
			countLines(opts);
		}
	}
	
	private void min(Options opts) {
		long min = 0;
		if (opts.getArgsSize() == 0) {
			MinCountAction action = new MinCountAction();
			eachLine(System.in, action);
			min = action.getMin();
		} else {
			for (String num : opts.getArgs()) {
				long v =  Long.parseLong(num);
				if (v < min) {
					min = v;
				}
			}
		}
		println(String.valueOf(min));		
	}

	private void max(Options opts) {
		long max = 0;
		if (opts.getArgsSize() == 0) {
			MaxCountAction action = new MaxCountAction();
			eachLine(System.in, action);
			max = action.getMax();
		} else {
			for (String num : opts.getArgs()) {
				long v =  Long.parseLong(num);
				if (v > max) {
					max = v;
				}
			}
		}
		println(String.valueOf(max));		
	}

	private void avgTotal(Options opts) {
		double avg = 0.0;
		long sum = 0;
		if (opts.getArgsSize() == 0) {
			LineSumAction sumAction = new LineSumAction();
			eachLine(System.in, sumAction);
			sum = sumAction.getSum();
			avg = sum * 1.0 / sumAction.getLineCount();
		} else {
			for (String num : opts.getArgs()) {
				sum += Long.parseLong(num);
			}
			avg = sum * 1.0 / opts.getArgsSize();
		}
		println(String.valueOf(avg));
	}

	private void countLines(Options opts) {
		if (opts.getArgsSize() == 0) {
			count(System.in, "STDIN", false);
		} else {
			for (String name : opts.getArgs()) {
				try {
					count(new FileInputStream(name), name, true);
				} catch (FileNotFoundException e) {
					throw new RuntimeException("File not found " + name);
				}
			}
		}
	}

	private void sumTotal(Options opts) {
		long sum = 0;
		if (opts.getArgsSize() == 0) {
			LineSumAction sumAction = new LineSumAction();
			eachLine(System.in, sumAction);
			sum = sumAction.getSum();
		} else {
			for (String num : opts.getArgs()) {
				sum += Long.parseLong(num);
			}
		}
		println(String.valueOf(sum));
	}

	private void count(InputStream ins, String filename, boolean printName) {
		debug("Processing input from " + filename);
		LineCountAction action = new LineCountAction();
		eachLine(ins, action);
		print(String.valueOf(action.getLineCount()));
		
		if (printName) {
			print("\t" + filename);
		}
		println("");
	}
	public static class MaxCountAction implements LineAction {
		protected long max = 0;
		
		public long getMax() {
			return max;
		}
		
		@Override
		public void onLine(String num) {
			long v =  Long.parseLong(num);
			if (v > max) {
				max = v;
			}
		}
	}
	public static class MinCountAction implements LineAction {
		protected long min = 0;
		
		public long getMin() {
			return min;
		}
		
		@Override
		public void onLine(String num) {
			long v =  Long.parseLong(num);
			if (v < min) {
				min = v;
			}
		}
	}
	public static class LineCountAction implements LineAction {
		protected long lineCount = 0;
		
		public long getLineCount() {
			return lineCount;
		}
		
		@Override
		public void onLine(String line) {
			lineCount ++;
		}
	}
	public static class LineSumAction extends LineCountAction {
		private long sum = 0;
		
		public long getSum() {
			return sum;
		}
		
		@Override
		public void onLine(String line) {
			lineCount ++;
			sum += Long.parseLong(line);
		}
	}
}