package ztool;

import static ztool.CliHelper.*;

public class Printf extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("format=<printf_fmt>",        "Specify custom format string.").
		setOption("types=<type_names>",         "Comma seperated type of: string, int or double.").
		setOption("useLineAsArgs=<numOfLine>",  "Use STDIN lines as argument to printf with numOfLine at times.").
		setSummary(
			"Print arguments or input lines onto screen using printf function." +
			"").
		setUsage("ztool Printf [Options] <text>\n").
		setExamples(
			"  ztool Printf one two\n" +
			"  ztool Printf --format=\"%s\\n%s\\n\" one two\n" +
			"  ztool Printf --format=\"%-5s %-5s %-9.1f\\n\" --types=\"string,string,double\" one two 45.56789 three four 0.3215\n" +
			"  ztool Range 1:50 | ztool Printf --format=\"%s\\t%s\\t%s\\t%s\\t%s\\n\" --useLineAsArgs=5\n" +
			"");
	}
	
	private String format;
	private String types;
	private int useLineAsArgs;
	
	private String[] typeNames;
	
	@Override
	public void run(Options opts) {
		format = opts.get("format", null);
		useLineAsArgs = opts.getInt("useLineAsArgs", -1);
		
		//Fix escaped characters by shell.		
		if (format != null) {
			types = opts.get("types", "");
			typeNames = types.split("\\s*,\\s*");
			
			format = unEscapeShellChars(format);	
			debug(">> format " + format);
			
			if (useLineAsArgs > 0) {
				if (types.equals("")) {
					typeNames = new String[useLineAsArgs];
					for (int i = 0; i < useLineAsArgs; i++) {
						typeNames[i] = "string"; //default.
					}
				}else  if (typeNames.length != useLineAsArgs) {
					throw new RuntimeException("The number of types " +
							typeNames.length + " is not matched to useLineAsArgs " + 
							useLineAsArgs);
				}
				debug(">> params.length " + useLineAsArgs);	
				LineAsArgAction lineAsArgAction = new LineAsArgAction();
				eachLine(System.in, lineAsArgAction);
				
				int paramsLeft = lineAsArgAction.getLineCount() % useLineAsArgs;
				if (paramsLeft != 0) {
					Object[] params = lineAsArgAction.getParams();
					for (int i = paramsLeft; i < useLineAsArgs; i++) {
						int typePos = i % typeNames.length;
						String type = typeNames[typePos];
						
						String value = "";
						if (type.equals("int")) {
							value = "0";
						} else if (type.equals("double")) {
							value = "0.0";
						}
						debug(">>Filling up missing params " + i + ", value=" + value);
						setParam(params, i, type, value);
					}
					debug(">>Printing " + format + ", params=" + params);
					System.out.printf(format, params);
				}
			} else {
				int argSize = opts.getArgsSize();
				if (types.equals("")) {
					typeNames = new String[argSize];
					for (int i = 0; i < argSize; i++) {
						typeNames[i] = "string"; //default.
					}
				}else if (argSize % typeNames.length != 0) {
					throw new RuntimeException("The number of types " +
							typeNames.length + " is not divisible by number of arguments " + 
							argSize);
				}
				
				//Work with command line arguments only.
				Object[] params = new Object[argSize];
				debug(">> params.length " + params.length);	
				for (int i = 0; i < argSize; i++) {
					String arg = opts.getArg(i);
					
					int typePos = i % typeNames.length;
					String type = typeNames[typePos];
					debug(">>arg[" + i +"]=" + arg + ", len=" + arg.length() + 
							", typePos=" + typePos + ", type=" + type);
					setParam(params, typePos, type, arg);
					
					if (typePos == typeNames.length - 1) {
						debug(">>Printing " + format + ", params=" + params);
						System.out.printf(format, params);
					}
				}
			}
		} else {
			for (String s : opts.getArgs()) {
				s = unEscapeShellChars(s);
				System.out.println(s);
			}
		}
	}
	
	private String unEscapeShellChars(String input) {
		debug(">> unEscapeShellChars input " + input);
		input = input.replaceAll("\\\\n", "\n");		
		input = input.replaceAll("\\\\t", "\t");
		return input;
	}

	private class LineAsArgAction implements LineAction {
		private int lineCount = 0;
		private Object[] params = new Object[useLineAsArgs];
		
		public int getLineCount() {
			return lineCount;
		}
		public Object[] getParams() {
			return params;
		}
		@Override
		public void onLine(String line) {
			if (line.trim().equals("")) {
				debug("Skipping empty line");
				return;
			}
			String type = "string";
			if (lineCount < typeNames.length) {
				type = typeNames[lineCount];
			}					
			int paramPos = lineCount % useLineAsArgs;
			debug(">>line[" + lineCount +"]=" + line + ", len=" + line.length() + 
					", type=" + type + ", paramPos=" + paramPos);
			setParam(params, paramPos, type, line);
			lineCount ++;						
			if (paramPos >= useLineAsArgs - 1) {
				debug(">>Printing " + format + ", params=" + params);
				System.out.printf(format, params);
			}
		}
	}

	private void setParam(Object[] params, int paramIdx, String type, String value) {
		if (type.equals("string")) {
			params[paramIdx] = value;
		} else if (type.equals("int")) {
			params[paramIdx] = Integer.parseInt(value);
		} else if (type.equals("double")) {
			params[paramIdx] = Double.parseDouble(value);
		} else {
			throw new RuntimeException("Unknow type for " + type + "(i=" + paramIdx + ")");
		}
	}
}
