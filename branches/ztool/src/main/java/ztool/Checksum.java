package ztool;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.Provider;
import java.security.Security;
import java.security.Provider.Service;
import java.util.HashSet;
import java.util.Set;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

public class Checksum extends CliBase {	
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("help", 	             "Display help page info.").
		setOption("method=<algorithm>",  "Change the checksum method. Default MD5.").
		setOption("listMethods",         "List available algorithm methods.").
		setSummary("Calculate a checksum value for each given file.\n").
		setUsage("ztool Checksum [Options] [file ...]\n").
		setExamples(
			"  ztool Checksum file1\n" +
			"  ztool Checksum file1 file2\n" +
			"");
	}
		
	private String method;
	
	@Override
	public void run(Options opts) {
		if (opts.has("listMethod")) {
			Set<String> names = new HashSet<String>();
			Provider[] providers = Security.getProviders();
			for (Provider provider : providers) {
				Set<Service> services = provider.getServices();
				for (Service srv : services) {
					if (!srv.getType().equals("MessageDigest")) {
						continue;
					}
					String algorithm = srv.getAlgorithm();
					if (!names.contains(algorithm)) {
						names.add(algorithm);
//						System.out.println("Method/Algorithm Name=" + 
//								algorithm + ", Provider=" + provider.getName());
						System.out.println(algorithm);
					}
				}
			}
			return; //exit program
		}
		
		method = opts.get("method", "MD5");
		for (String name : opts.getArgs()) {
			System.out.println(name + ": " + checkSum(name));
		}	
	}

	private String checkSum(String name) {
		FileInputStream fins = null; 
		try {
			fins = new FileInputStream(name);
			MessageDigest md = MessageDigest.getInstance(method);
			BufferedInputStream bins = new BufferedInputStream(fins);
			int MAX = 1024;
			byte[] buff = new byte[MAX];
			int len = -1;
			while ((len = bins.read(buff, 0, MAX)) != -1) {
				md.update(buff, 0, len);
			}

			byte[] digest = md.digest();
			StringBuilder sb = new StringBuilder();
			for (byte b : digest) {
				sb.append(String.format("%x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (fins != null) {
				try {
					fins.close();
				} catch (IOException e) {
					throw new RuntimeException("Failed to close file stream.", e);
				}
			}
		}
	}
}
