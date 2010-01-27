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

package jragonsoft.javautil.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility methods for manipulate Java regular expression.
 * 
 * @author zemian
 * @version $Id: RegexUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class RegexUtils {
	/**
	 * Description of the Method
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @param map
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String expandSubstitutions(String input, Properties map) {
		List names = getExpandableNames(input);
		final Pattern expVarPattern = Pattern
				.compile("(?<=^\\s*|[^\\$])(\\$\\{.+?\\})");
		Matcher expVarMatcher = expVarPattern.matcher(input);

		StringBuffer sb = new StringBuffer();
		int s = 0;
		int e = 0;
		int i = 0;
		while (expVarMatcher.find()) {
			//Found expandable var
			s = expVarMatcher.start(1);
			sb.append(input.substring(e, s));
			e = expVarMatcher.end(1);

			String replace = expVarMatcher.group(1);
			String key = (String) names.get(i++);

			//Checking for sustitution replacement.
			if (map.containsKey(key)) {
				replace = map.getProperty(key);
			}

			//Replace
			sb.append(replace);
		}
		sb.append(input.substring(e));
		return StringUtils.replace(sb.toString(), "$$", "$");
	}

	/**
	 * A supporting method for {@link #expandSubstitutions}method.
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @return The expandableNames value
	 */
	public static List getExpandableNames(String input) {
		List names = new ArrayList();
		final Pattern namePattern = Pattern
				.compile("(?<=^\\s*|[^\\$])\\$\\{(.+?)\\}");
		Matcher nameMatcher = namePattern.matcher(input);

		while (nameMatcher.find()) {
			names.add(nameMatcher.group(1));
		}
		return names;
	}

	/**
	 * Escape all regex chars (adding \\ before it.)
	 * <p>
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String escapeRegex(String source) {
		final Pattern regexEscaper = Pattern
				.compile("([^a-zA-z0-9]|\\\\|\\[|\\]|\\^)");
		return regexEscaper.matcher(source).replaceAll("\\\\$1");
	}

	/**
	 * Simple wrapper method to do regex find.
	 */
	public static boolean find(String source, String regex) {
		final Pattern pattern = Pattern.compile(regex);
		return pattern.matcher(source).find();
	}
}