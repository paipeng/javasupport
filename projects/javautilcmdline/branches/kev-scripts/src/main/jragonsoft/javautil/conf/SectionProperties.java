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

package jragonsoft.javautil.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This map contain map of map. Each section is a map of ConfigProperties
 * <p>
 * 
 * Sample file <br>
 * 
 * <pre>
 * 
 * [sect1]
 * foo = bar
 * foo2 = ${bar}
 * [sect2]
 * [sect3]
 * foo = bar
 * 
 * </pre>
 * 
 * <p>
 * 
 * Variable expansion only works within each section.
 * 
 * @author zemian
 * @version $Id: SectionProperties.java 42 2006-05-10 23:25:49Z zemian $
 * @see ConfigProperties
 */
public class SectionProperties extends TreeMap {
	/**
	 *  
	 */
	private static final long serialVersionUID = -1200036883035248667L;

	Pattern commentPattern = Pattern.compile("(^\\s*#)|(^\\s*$)");

	Pattern sectionPattern = Pattern.compile("\\s*\\[\\s*(\\w+)\\s*\\]\\s*");

	/**
	 * Gets the sections attribute of the SectionProperties object
	 * 
	 * @return The sections value
	 */
	public Set getSections() {
		return keySet();
	}

	/**
	 * Gets the section attribute of the SectionProperties object
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The section value
	 */
	public ConfigProperties getSection(String name) {
		return (ConfigProperties) get(name);
	}

	/**
	 * Description of the Method
	 * 
	 * @param inStream
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void load(InputStream inStream) throws IOException {
		String sectionName = null;
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				inStream));
		String line = null;
		while ((line = reader.readLine()) != null) {
			Matcher matcher = commentPattern.matcher(line);
			if (matcher.find()) {
				continue;
			}
			matcher = sectionPattern.matcher(line);
			if (matcher.matches()) {
				sectionName = matcher.group(1);
				put(sectionName, new ConfigProperties());
			} else {
				if (sectionName == null) {
					throw new IOException("Section is not defined.");
				}
				String[] pair = line.split("\\s*=\\s*");
				if (pair.length >= 2) {
					getSection(sectionName).setProperty(pair[0], pair[1]);
				}
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param outStream
	 *            Description of the Parameter
	 * @param header
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void store(OutputStream outStream, String header) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(outStream)));
		out.println("# Created On: " + new Date());
		if (header != null) {
			out.println(header);
		}
		out.println();
		for (Iterator itr = getSections().iterator(); itr.hasNext();) {
			String sectName = (String) itr.next();
			out.println("[ " + sectName + " ]");
			ConfigProperties p = getSection(sectName);
			for (Iterator itr2 = p.keySet().iterator(); itr2.hasNext();) {
				String name = (String) itr2.next();
				out.println(name + " = " + p.getProperty(name));
			}
			out.println();
		}
		out.flush();
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public String toString() {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(new BufferedWriter(writer));
		for (Iterator itr = getSections().iterator(); itr.hasNext();) {
			String sectName = (String) itr.next();
			out.println("[ " + sectName + " ]");
			out.println(getSection(sectName));
		}
		out.flush();
		return writer.getBuffer().toString();
	}
}
