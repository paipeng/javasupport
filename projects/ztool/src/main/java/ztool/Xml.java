package ztool;

import static ztool.CliHelper.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Xml extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	               "Display help page info.").
		setOption("splitSep=<regex>",      "Seperator use to split the input lines.").
		setOption("fieldTagName=<text>",   "Xml element name use for each field.").
		setOption("recordTagName=<text>",  "Xml element name use for each record.").
		setOption("recordsTagName=<text>", "Xml element name use for each records.").
		setOption("noSourceAttr", 	       "Do not add source attribute to records.").
		setOption("noLineNumAttr", 	       "Do not add lineNum attribute to record.").
		setOption("noFieldNumAttr", 	   "Do not add fieldNum attribute to field.").
		setOption("trimField", 	           "Trim field value before write to XML.").
		setSummary(
			"Convert flat file into xml document. This program will split every\n" +
			"input text line (from a file or STDIN) and output a well formed XML\n" +
			"record element.\n" +
			"").
		setUsage("ztool Xml [Options] [file ...]\n").
		setExamples(
			"  ztool Xml data.txt\n" +
			"  ztool Cat data.txt | ztool Xml\n" +
			"  ztool Sysinfo -s | " +
			"ztool Xml -s=\": \" " +
			"--trimField --recordTagName=\"property\" --fieldTagName=\"name,value\"\n" +
			"");
	}
	
	private String splitSep;
	private String fieldTagName;
	private String recordTagName;
	private String recordsTagName;
	private boolean noSourceAttr;
	private boolean noLineNumAttr;
	private boolean noFieldNumAttr;
	private boolean trimField;
	
	private String[] fieldTagNames;
	private boolean useMultipleNames; 
	private boolean skipBlankLine = true;
	
	@Override
	public void run(Options opts) {
		splitSep = opts.get("splitSep", "\\s+");
		fieldTagName = opts.get("fieldTagName", "field");
		recordTagName = opts.get("recordTagName", "record");
		recordsTagName = opts.get("recordsTagName", "records");
		noSourceAttr = opts.has("noSourceAttr");
		noLineNumAttr = opts.has("noLineNumAttr");
		noFieldNumAttr = opts.has("noFieldNumAttr");
		trimField = opts.has("trimField");
		
		//Check to see if user specify specific fieldTagName for each field
		// by splitting with comma
		fieldTagNames = fieldTagName.split(",");
		useMultipleNames = fieldTagNames.length > 1;
		
		if (opts.getArgsSize() == 0) {
			printRecordsXml("STDIN", System.in);
		} else {
			for (String name : opts.getArgs()) {
				try {
					printRecordsXml(name, new FileInputStream(name));
				} catch (FileNotFoundException e) {
					throw new RuntimeException("File not found " + name);
				}
			}
		}		
	}

	private void printRecordsXml(String sourceName, InputStream ins) {
		String sourceAttr = noSourceAttr ? "" : " source=\"" + sourceName + "\"";
		println("<" + recordsTagName + sourceAttr + ">");
		eachLine(ins, new LineAction() {
			int lineNum = 0;
			@Override
			public void onLine(String line) {
				if (skipBlankLine && line.trim().equals("")) {
					return;
				}
				
				lineNum ++;
				String lineNumAttr = noLineNumAttr ? "" : " lineNum=\"" + lineNum + "\"";
				println("  <" + recordTagName + lineNumAttr + ">");
				String[] fields = line.split(splitSep);
				
				int fieldNum = 0;
				for (String field : fields) {
					String tagName = useMultipleNames ? fieldTagNames[fieldNum] : fieldTagName; 
					String fieldNumAttr = noFieldNumAttr ? "" : " fieldNum=\"" + fieldNum + "\"";

					fieldNum ++;
					if (trimField) {
						field = field.trim();
					}			
					println("    <" + tagName + fieldNumAttr + ">" + field + "</" + tagName + ">");
				}
				println("  </" + recordTagName + ">");
			}
		});
		println("</" + recordsTagName + ">");
	}
}
