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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.support.GetOpt;
import jragonsoft.javautil.util.DateUtils;
import jragonsoft.javautil.util.StringUtils;


/**
 * Utility program. Use -h or --help option in command line to see help page.
 * 
 * @author zemian
 * @version $Id: MillisTime.java 4 2006-03-16 15:27:19Z zemian $
 */
public class MillisTime {
	private static SimpleDateFormat df;

	private static String timeStr;

	private static String formatStr;

	private static String tzID;

	private static long currentTime;

	/** Description of the Method */
	public static void printExitHelp() {
		String help = "USAGE:\n"
				+ "\n  MillisTime [options]"
				+ "\n  Print current time in milli second since Epoch, January 1, 1970 UTC/GMT."
				+ "\n  Options allow you to display and test different timezone and formats."
				+ "\n"
				+ "\n  [options]"
				+ "\n    --h or -h           Help and version."
				+ "\n    --printformat       Print all of time string format rules."
				+ "\n    --printtimezone     Print all avaible timezone IDs."
				+ "\n    --millis=MILLIS     Convert a millistime back to a time string."
				+ "\n    --elapse=MILLIS     Calculate ellapse time from MILLIS to CurrentTime."
				+ "\n    --timestr=TIMESTR   Change the value of CurrentTime from a time string."
				+ "\n    --timezone=TZID     Change the value of timezone. Default to GMT."
				+ "\n    --format=FORMAT     Change the format for time string. Default MM/dd/yyyy HH:mm:ss zzz"
				+ "\n" + "\nEXAMPLES:"
				+ "\n  $ # Assume using wrapper scripts and PATH is set"
				+ "\n  $ millistime"
				+ "\n  $ millistime --timestr=\"01/01/1970 00:00:00 GMT\""
				+ "\n  $ millistime --timestr=\"01/01/2006 00:00:00 EST\""
				+ "\n  $ millistime --millis=1136091600000"
				+ "\n  $ millistime --elapse=18000000"
				+ "\n  $ millistime --millis=`millistime` --timezone=EST"
				+ "\n" + "\nCREDITS:"
				+ "\n  ZMan Java Utility. <zemiandeng@gmail.com>"
				+ "\n  $Id: MillisTime.java 4 2006-03-16 15:27:19Z zemian $";

		System.out.println(help);
		System.exit(0);
	}

	/**
	 * The main program for the MillisTime class
	 * 
	 * @param args
	 *            The command line arguments
	 */
	public static void main(String[] args) throws Exception {
		GetOpt opt = new GetLongOpt(args);

		//Process options
		if (opt.isOpt("h") || opt.isOpt("help")) {
			printExitHelp();
		}

		if (opt.isOpt("printtimezone")) {
			printTimeZones();
			System.exit(0);
		} else if (opt.isOpt("printformat")) {
			printRules();
			System.exit(0);
		}

		timeStr = opt.getOpt("timestr", null);
		formatStr = opt.getOpt("format", "MM/dd/yyyy HH:mm:ss zzz");
		tzID = opt.getOpt("timezone", "GMT");
		TimeZone tz = TimeZone.getTimeZone(tzID);
		df = new SimpleDateFormat(formatStr);
		df.setTimeZone(tz);

		if (timeStr != null) {
			currentTime = df.parse(timeStr).getTime();
		} else {
			//currentTime = new Date().getTime();
			currentTime = System.currentTimeMillis();
		}

		if (opt.isOpt("elapse")) {
			long msecs = opt.getLongOpt("elapse", -1);
			if (msecs < 0)
				System.out.println("You need to give value >0");
			else
				System.out.println(DateUtils.getEllapseTimeString(msecs));
			System.exit(0);
		} else if (opt.isOpt("millis")) {
			long msecs = opt.getLongOpt("millis", -1);
			if (msecs < 0)
				System.out.println("You need to give value >0");
			else
				System.out.println(df.format(new Date(msecs)));
			System.exit(0);
		}

		//Normal default function, print current millis time.
		System.out.println(currentTime);
	}

	/** Description of the Method */
	public static void printRules() {
		String rules = ""
				+ "\nLetter   Date or Time Component  Presentation    Examples                                  "
				+ "\n======   ======================  ============    ========                                  "
				+ "\nG        Era designator          Text            AD                                        "
				+ "\ny        Year                    Year            1996; 96                                  "
				+ "\nM        Month in year           Month           July; Jul; 07                             "
				+ "\nw        Week in year            Number          27                                        "
				+ "\nW        Week in month           Number          2                                         "
				+ "\nD        Day in year             Number          189                                       "
				+ "\nd        Day in month            Number          10                                        "
				+ "\nF        Day of week in month    Number          2                                         "
				+ "\nE        Day in week             Text            Tuesday; Tue                              "
				+ "\na        Am/pm marker            Text            PM                                        "
				+ "\nH        Hour in day (0-23)      Number          0                                         "
				+ "\nk        Hour in day (1-24)      Number          24                                        "
				+ "\nK        Hour in am/pm (0-11)    Number          0                                         "
				+ "\nh        Hour in am/pm (1-12)    Number          12                                        "
				+ "\nm        Minute in hour          Number          30                                        "
				+ "\ns        Second in minute        Number          55                                        "
				+ "\nS        Millisecond             Number          978                                       "
				+ "\nz        Time zone               General time zone   Pacific Standard Time; PST; GMT-08:00 "
				+ "\nZ        Time zone               RFC 822 time zone   -0800                                 ";

		rules += "\n"
				+ "\nDate and Time Pattern             Results                               "
				+ "\n=====================             =======                               "
				+ "\n\"yyyy.MM.dd G 'at' HH:mm:ss z\"  2001.07.04 AD at 12:08:56 PDT         "
				+ "\n\"EEE, MMM d, ''yy\"              Wed, Jul 4, '01                       "
				+ "\n\"h:mm a\"                        12:08 PM                              "
				+ "\n\"hh 'o''clock' a, zzzz\"         12 o'clock PM, Pacific Daylight Time  "
				+ "\n\"K:mm a, z\"                     0:08 PM, PDT                          "
				+ "\n\"yyyyy.MMMMM.dd GGG hh:mm aaa\"  02001.July.04 AD 12:08 PM             "
				+ "\n\"EEE, d MMM yyyy HH:mm:ss Z\"    Wed, 4 Jul 2001 12:08:56 -0700        "
				+ "\n\"yyMMddHHmmssZ\"                 010704120856-0700                     ";

		System.out.println(rules);
	}

	/** Description of the Method */
	public static void printTimeZones() {
		String[] ids;

		ids = TimeZone.getAvailableIDs();
		System.out.println("=== All Avialables TimeZone Names/IDs ===");
		for (int i = 0; i < ids.length; i++) {
			System.out.println(ids[i]);
		}

		System.out.println("");
		System.out.println("=== Some Common US TimeZones ===");
		System.out.println("Universal Time, Coordinated (UTC)");
		System.out.println("Atlantic Standard Time      (AST)");
		System.out.println("Eastern Standard Time       (EST)");
		System.out.println("Central Standard Time       (CST)");
		System.out.println("Mountain Standard Time      (MST)");
		System.out.println("Pacific Standard Time       (PST)");
		System.out.println("Hawaii Standard Time        (HST)");

		System.out.println("");
		System.out.println("=== Current JVM System Default TimeZone ===");
		TimeZone defaultTZ = TimeZone.getDefault();
		System.out.println(defaultTZ.getID());
		System.out.println(defaultTZ.getDisplayName());
	}

	/**
	 * Description of the Method
	 * 
	 * @param dateFormat
	 *            Description of the Parameter
	 * @param tzID
	 *            Description of the Parameter
	 */
	public static void printTime(String dateFormat, String tzID) {
		df = new SimpleDateFormat(dateFormat);
		Date now = new Date();
		TimeZone timeZone = TimeZone.getDefault();
		if (StringUtils.isNotBlank(tzID)) {
			timeZone = TimeZone.getTimeZone(tzID);
		}

		df.setTimeZone(timeZone);
		//System.out.println(timeZone.getDisplayName() + " " + df.format(now));
		System.out.println(df.format(now));

		/*
		 * df.setTimeZone(TimeZone.getTimeZone("GMT"));
		 * System.out.println("Greenwich Mean Time (GMT): " + df.format(now));
		 * df.setTimeZone(TimeZone.getTimeZone("AST"));
		 * System.out.println("Atlantic Standard Time (AST): " +
		 * df.format(now)); df.setTimeZone(TimeZone.getTimeZone("EST"));
		 * System.out.println("Eastern Standard Time (EST): " + df.format(now));
		 * df.setTimeZone(TimeZone.getTimeZone("CST"));
		 * System.out.println("Central Standard Time (CST): " + df.format(now));
		 * df.setTimeZone(TimeZone.getTimeZone("MST"));
		 * System.out.println("Mountain Standard Time (MST): " +
		 * df.format(now)); df.setTimeZone(TimeZone.getTimeZone("PST"));
		 * System.out.println("Pacific Standard Time (PST): " + df.format(now));
		 * df.setTimeZone(TimeZone.getTimeZone("HST"));
		 * System.out.println("Hawaii Standard Time (HST): " + df.format(now));
		 * df.setTimeZone(TimeZone.getTimeZone("UTC"));
		 * System.out.println("Samoa standard time (UTC): " + df.format(now));
		 */
	}
}
