package ztool;

import static ztool.CliHelper.*;

import java.io.File;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class Sysinfo extends CliBase {

	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser.
		setOption("showSysProps", "Show Java System Properties only.").
		setOption("fullInfo",     "Show full system info.").
		setOption("path",         "Print PATH string with each entry per new line.").
		setSummary(
			"Display localhost system information.\n" +
			"").
		setExamples(
			"  ztool Sysinfo [Options]\n" +
			"  ztool Sysinfo --showSysProps\n" +
			"");
	}

	public static String PATH_SEP = System.getProperty("path.separator");
	private boolean fullInfo = false;
	
	@Override
	public void run(Options opts) {
		if (opts.has("printPath")) {
			printPath();
		} else if (opts.has("showSysProps")) {
			showSysProps();
		} else {
			fullInfo = opts.has("fullInfo");
			try {
				showLocalHostInfo();
			} catch (UnknownHostException e) {
				throw new RuntimeException(e);
			}
		}
	}
	
	private void printPath() {
		Map<String, String> env = System.getenv();
		String pathKey = null;
		for(String k : env.keySet()) {
			if (k.matches("(?i)PATH") ) {
				pathKey = k;
				break;
			}
		}
		if (pathKey == null) {
			throw new RuntimeException("No PATH env found.");
		}
		
		String[] paths = env.get(pathKey).split(PATH_SEP);
		
		for (String p : paths) {
			System.out.println(p);
		}
	}

	private void showLocalHostInfo() throws UnknownHostException {
		String fmt = "%-25s: %s\n";
		
		Runtime rt = Runtime.getRuntime();
		OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
		InetAddress addr = InetAddress.getLocalHost();

		printf(fmt, "Current Time",         new Date() + ", millis=" + System.currentTimeMillis());
		printf(fmt, "Hostname/IP",          addr.toString());
		printf(fmt, "OS Name",              System.getProperty("os.name"));
		printf(fmt, "OS Version",           osBean.getVersion());
		printf(fmt, "OS Path Level",        System.getProperty("sun.os.patch.level"));
		printf(fmt, "CPU Name",             System.getProperty("sun.cpu.isalist"));
		printf(fmt, "CPU Archecture",       osBean.getArch());
		printf(fmt, "CPU Count",            osBean.getAvailableProcessors());
		printf(fmt, "Java VM Vendor",       System.getProperty("java.vm.vendor"));
		printf(fmt, "Java Version",         System.getProperty("java.version"));
			
		if (fullInfo) {
			printf(fmt, "Java Home",            System.getProperty("java.home"));
			printf(fmt, "Java Default locale",       Locale.getDefault().toString());
			printf(fmt, "Java Default charset",      Charset.defaultCharset());
			printf(fmt, "Java User Home",            System.getProperty("user.home"));
			printf(fmt, "Java Used Memory",          rt.totalMemory() - rt.freeMemory());
			printf(fmt, "Java Free Memory",          rt.freeMemory());
			printf(fmt, "Java Total Memory",         rt.totalMemory());
			printf(fmt, "Java Max Memory",           rt.maxMemory());
			println("");
					
			File[] roots = File.listRoots();
			for (File root : roots) {
				printf(fmt, "File system root", root.getAbsolutePath());
				printf(fmt, "  Total space",    root.getTotalSpace());
				printf(fmt, "  Free space",     root.getFreeSpace());
				printf(fmt, "  Usable space",   root.getUsableSpace());
			}
			println("");
		}
	}

	private void showSysProps() {
		Set<String> keys = System.getProperties().stringPropertyNames();
		int maxLen = findMaxLen(keys);
		String fmt = "%" + maxLen + "s: %s\n";
		
		for (String key : keys) {
			String value = System.getProperty(key);
			if (!(key.equals("path.separator")) && value.contains(PATH_SEP)) {
				StringBuilder sb = new StringBuilder();
				int i = 0;
				for (String s : value.split(PATH_SEP)) {
					if ( i++ == 0) {
						sb.append(s + "\n");
					}						
					sb.append(String.format(fmt, "", s));
				}
				value = sb.toString();
			}
			printf(fmt, key, value);
		}
		
	}

	private int findMaxLen(Set<String> keys) {
		int max = 0;
		for (String key : keys) {
			if (key.length() > max) {
				max = key.length();
			}
		}
		return max;
	}

}
