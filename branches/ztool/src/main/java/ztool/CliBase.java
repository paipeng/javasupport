package ztool;

import static ztool.CliHelper.println;
import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public abstract class CliBase {
	protected boolean debug = false;	
	protected void debug(String msg) {
		if(debug) {
			println(msg);
		}
	}
	
	protected void runMain(String[] args) {
		OptionsParser optsParser = new OptionsParser();
		initOptionsParser(optsParser);
		if (!optsParser.getOptions().getOptionsMap().containsKey("help")) {
			optsParser.setOption("help",      "Display help page info.");
		}
		if (!optsParser.getOptions().getOptionsMap().containsKey("debug")) {
			optsParser.setOption("debug",     "Show extra debug info.");
		}
		Options opts = optsParser.parse(args);
		
		debug = opts.has("debug");
		debug("The opts object after parse: " + opts);
		
		if (opts.has("help")) {
			opts.showExitUsage();
		}
		run(opts);
	}
	
	public abstract void initOptionsParser(OptionsParser optsParser);
	public abstract void run(Options opts);
}
