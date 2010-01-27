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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Mostly static methods for handle String objects. Most of the method will
 * throw RuntimeException instead of checked ones, so the method is more
 * developer friendly.
 * 
 * @author zemian
 * @version $Id: StringUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class StringUtils {
	/**
	 * Parse a CSV string list of names into a List.
	 */
	public static List parseCSV(String csv) {
		String[] parts = csv.split("\\s*,\\s*");
		List strs = new ArrayList(parts.length);
		for (int i = 0, maxIndex = parts.length; i < maxIndex; i++) {
			if (parts[i].length() > 0) {
				strs.add(parts[i]);
			}
		}

		return strs;
	}

	/**
	 * Same as parseCSVInt(), but if string can't convert
	 * to integer, then it's silently droped and cont to next value.
	 */
	public static List parseCSVInt(String csv) {
		return parseCSVInt(csv, false);
	}

	/**
	 * Parse a CSV string list of integers into a List. 
	 * 	 
	 * The given string list may specify range in the format of START-END[:STEP], 
	 * and it's inclusive, and optional STEP is default to 1.
	 */
	public static List parseCSVInt(String csv, boolean raiseException) {
		String[] parts = csv.split("\\s*,\\s*");
		List nums = new ArrayList(parts.length);

		for (int i = 0, maxIndex = parts.length; i < maxIndex; i++) {
			String part = parts[i];
			try {
				if (part.indexOf("-") > 0) {
					//parse range of integers
					String range[] = StringUtils.split(part, "-");
					int start, end, step;
					start = Integer.parseInt(range[0]);
					if (range[1].indexOf(":") > 0) {
						String steps[] = StringUtils.split(range[1], ":");
						end = Integer.parseInt(steps[0]);
						step = Integer.parseInt(steps[1]);
					} else {
						end = Integer.parseInt(range[1]);
						step = 1;
					}
					for (int j = start; j <= end; j += step) {
						nums.add(new Integer(j));
					}
				} else {
					nums.add(new Integer(part));
				}
			} catch (Exception e) {
				if (raiseException) {
					throw new RuntimeException("Can't parse #" + i + " value: "
							+ part);
				}
			}
		}

		return nums;
	}

	/**
	 * same as parseCSVInt but return int array instead.
	 */
	public static int[] parseCSVIntArray(String csv) {
		List nums = parseCSVInt(csv);
		return ArrayUtils.toIntArray(nums);
	}
	
	/** Description of the Field */
	public final static char[] HEX_CHARS = 
			{ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
			  'A', 'B', 'C', 'D', 'E', 'F' };

	/**
	 * Convert bytes into Hex String
	 * 
	 * @param bytes
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public final static String toHexString(byte[] bytes) {
		int width = -1;// no column.
		return toHexString(bytes, 0, bytes.length, width);
	}

	/**
	 * Convert bytes into Hex String. Offset index and max len maybe given for
	 * input bytes array. Resulted string is TWO hex value per single BYTE. If
	 * column is set to <=0, then all bytes will be converted without
	 * formatting. Else string is formated into lines with column number of
	 * bytes per line, and each byte is seperated by SPACE.
	 * 
	 * @param bytes
	 *            Description of the Parameter
	 * @param off
	 *            Description of the Parameter
	 * @param len
	 *            Description of the Parameter
	 * @param column
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public final static String toHexString(byte[] bytes, int off, int len,
			int column) {
		StringBuffer sb = new StringBuffer();
		for (int i = off; i < len; i++) {
			sb.append(HEX_CHARS[(bytes[i] >> 4) & 0xF]);
			sb.append(HEX_CHARS[bytes[i] & 0xF]);
		}

		String hexString = sb.toString();
		if (column > 0) {
			column = column * 2;
			//Format hex string with space every two bytes and new line every
			// column of bytes.
			sb = new StringBuffer();
			int lastCol = column - 1;
			int strLen = hexString.length();
			for (int i = 0; i < strLen; i++) {
				sb.append(hexString.charAt(i));
				if (i % column == lastCol) {
					sb.append(FileUtils.LINE_SEP);
				} else if (i % 2 == 1) {
					sb.append(" ");
				}
			}
			hexString = sb.toString();
		}
		return hexString.trim();
	}

	/**
	 * pad SPACE in front of string until the length is maxLen. If string is
	 * longer than maxLen, return unchanged.
	 * 
	 * @param src
	 *            Description of the Parameter
	 * @param maxLen
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String pad(String src, int maxLen) {
		return padFrontChar(src, maxLen, ' ');
	}

	/**
	 * pad char ch in front of string until the length is maxLen. If string is
	 * longer than maxLen, return unchanged.
	 * 
	 * @param src
	 *            Description of the Parameter
	 * @param maxLen
	 *            Description of the Parameter
	 * @param ch
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String padFrontChar(String src, int maxLen, char ch) {
		if (src.length() >= maxLen) {
			return src;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = maxLen - src.length(); i > 0; i--) {
			sb.append(ch);
		}
		sb.append(src);
		return sb.toString();
	}

	/**
	 * pad char ch in front of string until the length is maxLen. If string is
	 * longer than maxLen, return unchanged.
	 * 
	 * @param src
	 *            Description of the Parameter
	 * @param maxLen
	 *            Description of the Parameter
	 * @param ch
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String padBackChar(String src, int maxLen, char ch) {
		if (src.length() >= maxLen) {
			return src;
		}
		StringBuffer sb = new StringBuffer(src);
		for (int i = maxLen - src.length(); i > 0; i--) {
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * Join a list of String with delimeter in between. Return EMPTY String if
	 * list size is zero. If element is NULL, then it's converted ot empty
	 * String.
	 * 
	 * @param delim
	 *            Description of the Parameter
	 * @param items
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String join(String delim, List items) {

		if (items.size() <= 0 || delim == null) {
			return "";
		}

		StringBuffer ret = new StringBuffer();
		Object val = items.get(0);
		ret.append(val == null ? "" : val.toString());
		for (int i = 1, maxitems = items.size(); i < maxitems; i++) {
			val = items.get(i);
			ret.append(delim + (val == null ? "" : val.toString()));
		}
		return ret.toString();
	}

	/**
	 * Join a array of String of items with delimeter in between. Return EMPTY
	 * String if list size is zero.
	 * 
	 * @param delim
	 *            Description of the Parameter
	 * @param items
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String join(String delim, String[] items) {
		return join(delim, Arrays.asList(items));
	}

	/**
	 * Join a Map of items into like "key=value,key=value ..." format. Where in
	 * this example the delim is ",", and pairDelim is "=". The pair in map
	 * entry is looped using an Iterator. So use TreeMap if you want pair in
	 * order, else it's random.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @param pairDelim
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String join(Map map, String delim, String pairDelim) {

		if (map.size() <= 0) {
			return "";
		}
		ArrayList pairList = new ArrayList();
		Set entries = map.entrySet();
		Iterator itr = entries.iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Map.Entry) itr.next();
			pairList.add(entry.getKey() + pairDelim + entry.getValue());
		}

		return join(delim, pairList);
	}

	/**
	 * Same as split, but split all.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] split(String source, String delim) {
		return split(source, delim, 0);
	}

	/**
	 * A faster version of split than String.split(). The speed gained from NOT
	 * using any regex. It will spliting only max splitted items from a string.
	 * All unsplitted will the in the last item. If string is empty or null,
	 * return empty String array(String[0]). If string contains no delim or
	 * delim is empty, return the orig string as first item. If maxNumSplit is
	 * less or equals to zero, all items is splitted.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @param maxNumSplit
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] split(String source, String delim, int maxNumSplit) {
		final int SIZE = 10;
		int index = 0;
		String[] result = new String[SIZE];

		if (delim == null || delim.length() == 0) {
			return new String[] { source };
		}

		int delimLen = delim.length();
		int nextIndex = 0;
		int foundIndex = -1;
		int resultArraySize = result.length;
		while ((foundIndex = source.indexOf(delim, nextIndex)) != -1) {
			if (index == resultArraySize) {
				//expand array
				String[] temp = result;
				result = new String[result.length + 10];
				System.arraycopy(temp, 0, result, 0, temp.length);
				resultArraySize = result.length;
			}
			result[index++] = source.substring(nextIndex, foundIndex);
			nextIndex = foundIndex + delimLen;
			if (maxNumSplit > 0 && index >= maxNumSplit) {
				break;
			}
		}

		//repack array to just the right size!
		if (result.length >= index) {
			String[] temp = result;
			result = new String[index + 1];
			System.arraycopy(temp, 0, result, 0, index);
		}

		// the last token, or the whole line if no delim found.
		result[index++] = source.substring(nextIndex);

		return result;
	}

	/**
	 * Same as split with all splitted items trimed.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] splitTrimed(String source, String delim) {
		String[] items = split(source, delim);
		for (int i = items.length - 1; i >= 0; i--) {
			items[i] = items[i].trim();
		}
		return items;
	}

	/**
	 * Split a string in this format "key=value,key=value ..." into HashMap
	 * items. Where in this example the delim is ",", and pairDelim is "=". Pair
	 * that missing pairDelim will be dropped!
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @param pairDelim
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static Properties split(String source, String delim, String pairDelim) {
		Properties map = new Properties();
		String pairs[] = split(source, delim);
		for (int i = pairs.length - 1; i >= 0; i--) {
			String data[] = split(pairs[i], pairDelim, 1);
			if (data.length == 2) {
				map.setProperty(data[0], data[1]);
			}
		}
		return map;
	}

	/**
	 * Split and trim only the key used in map, not the value in map.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @param pairDelim
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static Properties splitTrimed(String source, String delim,
			String pairDelim) {
		Properties map = new Properties();
		String pairs[] = splitTrimed(source, delim);
		for (int i = pairs.length - 1; i >= 0; i--) {
			String data[] = split(pairs[i], pairDelim, 1);
			if (data.length == 2) {
				map.setProperty(data[0], data[1]);
			}
		}
		return map;
	}

	/**
	 * Spliting into like HashMap item, but only return a single matched value
	 * using a key.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param delim
	 *            Description of the Parameter
	 * @param pairDelim
	 *            Description of the Parameter
	 * @param key
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String splitMatch(String source, String delim,
			String pairDelim, String key) {

		Map map = split(source, delim, pairDelim);
		return (String) map.get(key);
	}

	/**
	 * Negate of isBlank()
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return The notBlank value
	 */
	public static boolean isNotBlank(String source) {
		return !isBlank(source);
	}

	/**
	 * Determine if string is BLANK or not. Here is the rules: If null, "", or
	 * if all chars in string contain WHITESPACE, it's BLANK!
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @return The blank value
	 */
	public static boolean isBlank(String source) {
		if (source == null) {
			return true;
		}
		int len = source.length();
		if (len == 0) {
			return true;
		}
		int whitespaceCount = 0;
		for (int i = len - 1; i >= 0; i--) {
			if (Character.isWhitespace(source.charAt(i))) {
				whitespaceCount++;
			}
		}

		if (whitespaceCount == len) {
			return true;
		}

		return false;
	}

	/**
	 * Remove front part of a string
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param front
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String trimFront(String source, String front) {
		if (source.startsWith(front)) {
			return source.substring(front.length());
		}

		return source;
	}

	/**
	 * Remove backend part of a string
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param back
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String trimBack(String source, String back) {
		if (source.endsWith(back)) {
			return source.substring(0, source.lastIndexOf(back));
		}

		return source;
	}

	/**
	 * This replace will not use regex like {@link String#split}does. It does
	 * straight text find and do replace without any wildcard.
	 * 
	 * @param source
	 *            Description of the Parameter
	 * @param find
	 *            Description of the Parameter
	 * @param replace
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String replace(String source, String find, String replace) {
		String[] parts = split(source, find);
		return join(replace, parts);
	}

	/**
	 * Determine whether the whole string contain all digit characters for an
	 * positive number.
	 */
	public static boolean isNumber(String s) {
		for (int i = s.length() - 1; i >= 0; i--) {
			if (!Character.isDigit(s.charAt(i)))
				return false;
		}
		return true;
	}

	/** Round a decimal to arbituary digit of decimals. the result is in string. */
	public static String round(double value, int decimalPlace) {
		StringBuffer numOfDecimal = new StringBuffer();
		for (int i = decimalPlace; i > 0; i--)
			numOfDecimal.append("0");
		if (decimalPlace > 0)
			numOfDecimal.insert(0, ".");
		NumberFormat nf = new DecimalFormat("#" + numOfDecimal.toString());
		return nf.format(value);
	}
	
	public static final DecimalFormat currencyNumberFormat = new DecimalFormat("#.00");
	/** Round a decimal to arbituary digit of decimals. the result is in string. */
	public static String roundCurrency(double value) {		
		return currencyNumberFormat.format(value);
	}

	/** Return a object string. empty if null. */
	public static String toString(Object value) {
		return (value == null) ? "" : value.toString();
	}

	/** Convert boolean to string. */
	public static String toString(double value) {
		return String.valueOf(value);
	}

	/** Convert boolean to string. */
	public static String toString(int value) {
		return String.valueOf(value);
	}

	/** Convert boolean to string. */
	public static String toString(boolean value) {
		return String.valueOf(value);
	}

	/** Convert string to double, if string is empty, returns 0.0. */
	public static double toDouble(String value) {
		if (isEmpty(value))
			return 0.0;
		return Double.parseDouble(value);
	}

	/** Convert string to int, if string is empty, returns 0. */
	public static int toInt(String value) {
		if (isEmpty(value))
			return 0;
		return Integer.parseInt(value);
	}

	/** Convert string to boolean, if string is empty, returns false. */
	public static boolean toBoolean(String value) {
		if (isEmpty(value))
			return false;
		return Boolean.valueOf(value).booleanValue();
	}

	/**
	 * If given string is null or empty with zero ro more spaces, then return
	 * true.
	 */
	public static boolean isEmpty(String str) {
		if (str == null)
			return true;
		else if (str.trim().equals(""))
			return true;
		return false;
	}

	/** Reverse of #isEmpty() */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}
}
