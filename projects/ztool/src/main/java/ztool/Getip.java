package ztool;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Getip extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	          "Display help page info.").
		setSummary(
			"Get IP addresses for given hostnames.\n" +
			"").
		setUsage("ztool Getip [Options] [hostname ...]\n").
		setExamples(
			"  ztool Getip localhost\n" +
			"  ztool Getip server1 server2\n" +
			"");
	}
		
	@Override
	public void run(Options opts) {
		List<String> hosts = opts.getArgs();
		if(opts.getArgsSize() == 0) {
			hosts.add("localhost");
		}
		
		for (String name : opts.getArgs()) {
			try {
				InetAddress addr = null;
				if (name.trim().equals("localhost")) {
					addr = InetAddress.getLocalHost();
				} else {
					addr = InetAddress.getByName(name);
				}
				
				String hostname = addr.getHostName();
				if (hostname.equals(name)) {
					hostname = "";
				}
				System.out.printf("%-15s %s %s\n", 
						          addr.getHostAddress(), 
						          name, 
						          hostname);
			} catch (UnknownHostException e) {
				System.out.printf("%-15s %s\n", "-", "Host " + name + " not found.");
			}
		}
	}
}
