/*
 *	  5/4/2006 Zemian Deng
 * 
 *	  Licensed under the Apache License, Version 2.0 (the "License");
 *	  you may not use this file except in compliance with the License.
 *	  You may obtain a copy of the License at
 * 
 *		  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *	  Unless required by applicable law or agreed to in writing, software
 *	  distributed under the License is distributed on an "AS IS" BASIS,
 *	  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *	  See the License for the specific language governing permissions and
 *	  limitations under the License.
 *																				 
 */

package jragonsoft.javautil.cmdtool;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.StringUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: GetIPHost.java 4 2006-03-16 15:27:19Z zemian $
 */
public class GetIPHost {
	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:"
				+ "\n  GetIPHost [options] HostOrIP [HostOrIP ...]"
				+ "\n  Get and print Hostname's IP address or vice versa."
				+ "\n" + "\n  [options]" + "\n    -h      Help and version."
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ getiphost" + "\n  $ getiphost localhost zem-toy"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: GetIPHost.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the GetIPHost class
	 * 
	 * @param args
	 *            The command line arguments
	 * @exception Exception
	 *                Description of the Exception
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetOpt(args);

		//Process options
		if (opt.isOpt("h")) {
			printExitHelp();
		}
		List hostList = new ArrayList();
		if (opt.getArgsCount() == 0) {
			hostList.add(InetAddress.getLocalHost());
		} else {
			for (int i = 0; i < opt.getArgsCount(); i++) {
				hostList.add(InetAddress.getByName(opt.getArg(i)));
			}
		}

		int count = 0;
		for (Iterator itr = hostList.iterator(); itr.hasNext();) {
			if (count++ > 0) {
				System.out.println("=====");
			}
			InetAddress host = (InetAddress) itr.next();
			System.out.println("Hostname: " + host.getHostName());
			//System.out.println("Address: " + host);
			System.out.println("Address: " + getIPString(host.getAddress()));
			System.out.println("CanonicalHostName: "
					+ host.getCanonicalHostName());
		}
	}

	/**
	 * Gets the iPString attribute of the GetIPHost class
	 * 
	 * @param addr
	 *            Description of the Parameter
	 * @return The iPString value
	 */
	private static String getIPString(byte[] addr) {
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < addr.length; i++) {
			String s = StringUtils.toHexString(new byte[] { addr[i] });
			int ad = Integer.parseInt(s, 16);
			sb.append(ad);
			if (i < addr.length - 1) {
				sb.append(".");
			}
		}
		return sb.toString();
	}
}
