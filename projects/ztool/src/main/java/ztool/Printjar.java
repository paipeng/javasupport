package ztool;

import static ztool.CliHelper.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Printjar extends CliBase {	
	private boolean showPathOnly = false;

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help",         "Display this help page.").
		setOption("debug",        "Show extra debug info.").
		setOption("showPathOnly", "Display list of entry path only.").
		setSummary(
			"Print one or more text file resources content from a jar file.\n" +
			"If no entry path is given, it default to it's manifest file.\n" +
			"").
		setUsage("Printjar [Options] <jarFile> [entryPath ...]").
		setExamples(
			"  ztool Printjar ztool-*.jar -s\n" +
			"  ztool Printjar ztool-*.jar\n" +
			"  ztool Printjar ztool-*.jar META-INF/MANIFEST.MF\n" +
		"");
		
	}

	@Override
	public void run(Options opts) {
		try {		
			List<String> args = opts.getArgs();
			if (args.size() == 0) {
				exit("You need to specify a jar file.");
			}
			JarFile jarFile = new JarFile(args.remove(0));
			if (args.size() == 0) {
				args.add("META-INF/MANIFEST.MF");
			}
			debug("Entry paths " + args);
			printJar(jarFile, new HashSet<String>(args));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}	
	}
	
	private void printJar(JarFile jarFile, Set<String> entryPaths) {
		debug("Unique entry paths " + entryPaths);
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) {
			JarEntry elem = entries.nextElement();
			String name = elem.getName();
			debug("Processing " + name);
			if (showPathOnly) {
				println(name);
			} else {
				if (entryPaths.contains(name)) {
					if (entryPaths.size() > 1) {
						println("Entry: " + name);
					}
					InputStream ins = null;
					try {
						ins = jarFile.getInputStream(elem);
						println(readText(ins));
					} catch (IOException e) {
						throw new RuntimeException(e);
					} finally {
						if (ins != null)
							try {
								ins.close();
							} catch (IOException e) {
								throw new RuntimeException(e);
							}
					}
				}
			}
		}
	}
}
