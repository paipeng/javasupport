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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provide methods to retrive map's value without casting. If key name doesn't
 * exist, it will return a sensable default value. If any error occur during
 * value casting, it's up to user to catch. Possible NullPointerException and/or
 * NumberFormationException for example.
 * 
 * @author zemian
 * @version $Id: MapUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class MapUtils {
	/**
	 * Get a Map within map. Return a new empty HashMap if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The map value
	 */
	public static Map getMap(Map map, Object name) {
		return (map.containsKey(name)) ? (Map) map.get(name) : new HashMap();
	}

	/**
	 * Get a List within map. Return a new empty ArrayList if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The map value
	 */
	public static List getList(Map map, Object name) {
		return (map.containsKey(name)) ? (List) map.get(name) : new ArrayList();
	}

	/**
	 * Gets a string value in a map. return empty string if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The stringMap value
	 */
	public static String getString(Map map, Object name) {
		return (map.containsKey(name)) ? map.get(name).toString() : "";
	}

	/**
	 * Gets a boolean value in a map. return false if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The booleanMap value
	 */
	public static boolean getBoolean(Map map, Object name) {
		return (map.containsKey(name)) ? Boolean.valueOf(
				map.get(name).toString()).booleanValue() : false;
	}

	/**
	 * Gets a int value in a map. return 0 if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The intMap value
	 */
	public static int getInt(Map map, Object name) {
		return (map.containsKey(name)) ? Integer.parseInt(map.get(name)
				.toString()) : 0;
	}

	/**
	 * Gets a long value in a map. return 0 if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The longMap value
	 */
	public static long getLong(Map map, Object name) {
		return (map.containsKey(name)) ? Long.parseLong(map.get(name)
				.toString()) : 0;
	}

	/**
	 * Gets a double value in a map. return 0.0 if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @return The doubleMap value
	 */
	public static double getDouble(Map map, Object name) {
		return (map.containsKey(name)) ? Double.parseDouble(map.get(name)
				.toString()) : 0.0;
	}

	/**
	 * Gets a String value in a map. return given def if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The stringMap value
	 */
	public static String getString(Map map, Object name, String def) {
		return (map.containsKey(name)) ? map.get(name).toString() : def;
	}

	/**
	 * Gets a boolean value in a map. return given def if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The booleanMap value
	 */
	public static boolean getBoolean(Map map, Object name, boolean def) {
		return (map.containsKey(name)) ? Boolean.valueOf(
				map.get(name).toString()).booleanValue() : def;
	}

	/**
	 * Gets a int value in a map. return given def if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The intMap value
	 */
	public static int getInt(Map map, Object name, int def) {
		return (map.containsKey(name)) ? Integer.parseInt(map.get(name)
				.toString()) : def;
	}

	/**
	 * Gets a long value in a map. return given def if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The longMap value
	 */
	public static long getLong(Map map, Object name, long def) {
		return (map.containsKey(name)) ? Long.parseLong(map.get(name)
				.toString()) : def;
	}

	/**
	 * Gets a double value in a map. return given def if not found.
	 * 
	 * @param map
	 *            Description of the Parameter
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The doubleMap value
	 */
	public static double getDouble(Map map, Object name, double def) {
		return (map.containsKey(name)) ? Double.parseDouble(map.get(name)
				.toString()) : def;
	}
}