package ztool;

import static ztool.CliHelper.exit;
import static ztool.CliHelper.matchGroups;
import static ztool.CliHelper.println;

import java.io.File;
import java.net.URI;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Help extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help",      "Display this help page.").
		setOption("debug",     "Show extra debug info.").
		setSummary(
			"ztool contains many command line utilities that process text and files,\n" +
			"like those basic coreutil commands found in Unix/Linx environment.\n" +
			"Since all ztool programs are written in Java, you may run it on any OS\n" +
			"platform that supports Java Virtual Machine (JRE) 1.5 or higher.\n" +
			"\n" +
			"This particular Help program will list all commands available in the\n" +
			"package library.\n" +
			"\n" +
			"== About help format and syntaxs:\n" +
			"  All format rule written on help page use the bracket([]) to mean optional\n" +
			"  input, and tag(<>) means required. And they may be nested. An eclipse(...)\n" +
			"  within them means that they can be repeated.\n" +
			"\n" +
			"== About Options:\n" +
			"  All programs in this package support a --help option unless stated\n" +
			"  otherwise. And option flag can be enter by long (two dashes) or\n" +
			"  short (one dash) using the first character of the long flag. If there\n" +
			"  are duplicated short flag, then only first one will be used, and the\n" +
			"  rest must use long flag instead.\n" +
			"\n" +
			"  If option value are given, it must be follow by equal char(=) then a\n" +
			"  value, and no space is allowed in between.\n" +
			"\n" +
			"  You may escape all option parsing from rest of arguments manually with\n" +
			"  a double dash (--).\n" +
			"\n" +
			"== Installation:\n" +
			"  If you have the ztool.jar file, then you may invoke a command like so:\n" +
			"    java -cp ztool.jar ztool.Run Sysinfo # Print system info.\n" +
			"    java -cp ztool.jar ztool.Cat *.txt      # Print all text files on screen.\n" +
			"    java -cp ztool.jar ztool.Run Help       # Display all available commands.\n" +
			"\n" +
			"  You may also create a shell script named ztool (ztool.bat for Windows) that\n" +
			"  can be added to your system path. Here is how a Windows script looks like:\n" +
			"    @echo off\n" +
			"    set SCRIPT_DIR=%~dp0\n" +
			"    java -cp \"%SCRIPT_DIR%\\*\" ztool.Run %*\n" +
			"\n" +
			"  Now you may call programs like this:\n" +
			"    ztool Sysinfo\n" +
			"    ztool Sysinfo -s | ztool Grep name\n" +
			"\n" +
			"== Author:\n" +
			"  ztool package is created by Zemian Deng.\n" +
			"").
		setUsage("Help [Options] ").
		setExamples(
			"  ztool Help\n" +
			"  ztool Help --help\n" +
		"");
	}

	@Override
	public void run(Options opts) {		
		try {
	        println("Available commands.");
			URI sourceURI = this.getClass().getProtectionDomain()
				.getCodeSource().getLocation().toURI();
			debug(">> sourceURI " + sourceURI);
			File sourceFile = new File(sourceURI);		
			if (sourceFile.isFile() && sourceFile.getName().endsWith(".jar")) {
				debug(">> jar filename " + sourceFile);
				JarFile jarFile = new JarFile(sourceFile);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry jarEntry = entries.nextElement();
					String name = jarEntry.getName();
	    			debug(">> jarEntry " + name);
					if (name.contains("ztool/")) {
						showCommandName(name);
					}
				}
	        } else if (sourceFile.isDirectory()){    		
	    		File sourceDir = new File(sourceFile, "ztool");
	    		debug(">> file " + sourceDir);
	    		if (!sourceDir.isDirectory()) {
		    		throw new RuntimeException("The source path is not a directory. " + sourceDir);
	    		}
	    		String[] names = sourceDir.list();
	            for (String name : names) {
					showCommandName(name);
	            }
	        } else {
	        	exit("Unkown package sourceURI: " + sourceURI);
	        }

	        println("");
	        println("To run one of these Commands try");
	        println("  java -cp ztool.jar ztool.Run Sysinfo");
	        println("");
	        println("Or if you have a wrapper script named ztool:");
	        println("  ztool Sysinfo");
	        println("");
	        println("Run Help --help to see more details.");
	        println("");
			println("ztool package is created by Zemian Deng.");
	        println("");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void showCommandName(String name) {
		if (!name.startsWith("/")) {
			name = "/" + name;
		}
		
		debug("Class name: " + name);
        
		/* we only want top single class name (no innser class) */
		List<String> groups = matchGroups(".*/(\\w+)\\.class", name);
    	if (groups.size() > 0) {
    		String cmd = groups.get(0);
    		debug("Command " + cmd);
    		if (cmd.equals("Run")  || 
    				cmd.equals("CliHelper") ||
    				cmd.equals("CliBase") ||
    				cmd.equals("Bible")) {
    			return;
    		}
    		println("  " + cmd);
    	}
	}
}
