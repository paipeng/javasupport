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
package jragonsoft.javautil.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A proxy for {@link java.util.Map} objects. This class provide many convient
 * method to return value that's already casted for you. It also provide
 * easy method to return a default value if key name is not found.
 */
public class EasyMap implements Map {
	private Map proxyMap;

	public EasyMap() {
		this.proxyMap = new HashMap();
	}

	public EasyMap(Map proxyMap) {
		this.proxyMap = proxyMap;
	}

	public int size() {
		return proxyMap.size();
	}

	public boolean isEmpty() {
		return proxyMap.isEmpty();
	}

	public boolean containsKey(Object key) {
		return proxyMap.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return proxyMap.containsValue(value);
	}

	public Object get(Object key) {
		return proxyMap.get(key);
	}

	public Object put(Object key, Object value) {
		return proxyMap.put(key, value);
	}

	public Object remove(Object key) {
		return proxyMap.remove(key);
	}

	public void putAll(Map t) {
		proxyMap.putAll(t);
	}

	public void clear() {
		proxyMap.clear();
	}

	public Set keySet() {
		return proxyMap.keySet();
	}

	public Collection values() {
		return proxyMap.values();
	}

	public Set entrySet() {
		return proxyMap.entrySet();
	}

	/** return the original proxyMap that's been proxy with. */
	public Map getProxyMap() {
		return this.proxyMap;
	}

	/** set the proxy proxyMap. */
	public void setProxyMap(Map proxyMap) {
		this.proxyMap = proxyMap;
	}

	/**
	 * Get a Map within proxyMap and return it as EasyMap. If no value found
	 * under the name key, a empty HashMap to a new EasyMap instance is returned.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return a EasyMap instance that wraps the inner map.
	 */
	public EasyMap getMap(Object name) {
		Map innerMap = (proxyMap.containsKey(name)) ? (Map) proxyMap.get(name)
				: new HashMap();
		if (innerMap instanceof EasyMap)
			return (EasyMap) innerMap;

		return new EasyMap((Map) innerMap);
	}

	/**
	 * Get a List within proxyMap. Return a new empty ArrayList if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return a List value
	 */
	public List getList(Object name) {
		return (proxyMap.containsKey(name)) ? (List) proxyMap.get(name)
				: new ArrayList();
	}

	/**
	 * Gets a string value in a proxyMap. return empty string if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return a String value
	 */
	public String getString(Object name) {
		return (proxyMap.containsKey(name)) ? proxyMap.get(name).toString()
				: "";
	}

	/**
	 * Gets a boolean value in a proxyMap. return false if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The booleanMap value
	 */
	public boolean getBoolean(Object name) {
		return (proxyMap.containsKey(name)) ? Boolean.valueOf(
				proxyMap.get(name).toString()).booleanValue() : false;
	}

	/**
	 * Gets a int value in a proxyMap. return 0 if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The intMap value
	 */
	public int getInt(Object name) {
		return (proxyMap.containsKey(name)) ? Integer.parseInt(proxyMap.get(
				name).toString()) : 0;
	}

	/**
	 * Gets a long value in a proxyMap. return 0 if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The longMap value
	 */
	public long getLong(Object name) {
		return (proxyMap.containsKey(name)) ? Long.parseLong(proxyMap.get(name)
				.toString()) : 0;
	}

	/**
	 * Gets a double value in a proxyMap. return 0.0 if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @return The doubleMap value
	 */
	public double getDouble(Object name) {
		return (proxyMap.containsKey(name)) ? Double.parseDouble(proxyMap.get(
				name).toString()) : 0.0;
	}

	/**
	 * Gets a String value in a proxyMap. return given def if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The stringMap value
	 */
	public String getString(Object name, String def) {
		return (proxyMap.containsKey(name)) ? proxyMap.get(name).toString()
				: def;
	}

	/**
	 * Gets a boolean value in a proxyMap. return given def if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The booleanMap value
	 */
	public boolean getBoolean(Object name, boolean def) {
		return (proxyMap.containsKey(name)) ? Boolean.valueOf(
				proxyMap.get(name).toString()).booleanValue() : def;
	}

	/**
	 * Gets a int value in a proxyMap. return given def if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The intMap value
	 */
	public int getInt(Object name, int def) {
		return (proxyMap.containsKey(name)) ? Integer.parseInt(proxyMap.get(
				name).toString()) : def;
	}

	/**
	 * Gets a long value in a proxyMap. return given def if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The longMap value
	 */
	public long getLong(Object name, long def) {
		return (proxyMap.containsKey(name)) ? Long.parseLong(proxyMap.get(name)
				.toString()) : def;
	}

	/**
	 * Gets a double value in a proxyMap. return given def if not found.
	 * 
	 * @param name
	 *            Description of the Parameter
	 * @param def
	 *            Description of the Parameter
	 * @return The doubleMap value
	 */
	public double getDouble(Object name, double def) {
		return (proxyMap.containsKey(name)) ? Double.parseDouble(proxyMap.get(
				name).toString()) : def;
	}
}
