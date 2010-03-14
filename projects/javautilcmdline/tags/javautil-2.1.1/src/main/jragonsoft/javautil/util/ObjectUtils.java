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

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/** Return an exploded field values of an arbitury object. */
public class ObjectUtils {
	public static String exploreFields(Object obj) {
		if (obj == null) {
			return "null";
		}

		StringBuffer sb = new StringBuffer();
		Class objClazz = obj.getClass();

		sb.append(getObjectID(obj)).append("\n");

		List allClasses = getAllSuperclasses(objClazz);
		for (Iterator itr = allClasses.iterator(); itr.hasNext();) {
			appendAllFields(sb, (Class) itr.next(), obj);
		}
		return sb.toString();
	}

	/** Return an exploded beab properties with values of an arbitury object. */
	public static String exploreProperties(Object obj) {
		throw new RuntimeException("Not yet implemented");
	}
	
	/** return an obejct's class_name@IDXXXX form of any object, even when it's toString()
	 * has been overriden. */
	public static String getObjectID(Object obj) {
		return obj.getClass().getName() + "@"
				+ Integer.toHexString(System.identityHashCode(obj));
	}

	/** For all fields in obj, append all values into the sb buffer. */
	public static void appendAllFields(StringBuffer sb, Class objClazz,
			Object obj) {
		Field[] fields = objClazz.getDeclaredFields();
		AccessibleObject.setAccessible(fields, true);
		for (int i = 0, max = fields.length; i < max; i++) {
			try {
				Field f = fields[i];
				String name = f.getName();
				sb.append("  " + name);
				sb.append(" : " + f.getType() + " = ");
				Object objValue = f.get(obj);
				sb.append(objValue == null ? "null" : objValue.toString())
						.append("\n");
			} catch (Exception e) {
				//throw new RuntimeException(e);
				sb.append("ERROR READING FIELD VALUE!").append("\n");
				continue;
			}
		}
	}

	/** A method borrowed from apache common-lang's ClassUtils. 
	 * Return all super classes of an obj class.*/
	public static List getAllSuperclasses(Class cls) {
		if (cls == null) {
			return null;
		}
		List classes = new ArrayList();
		Class superclass = cls.getSuperclass();
		while (superclass != null) {
			classes.add(superclass);
			superclass = superclass.getSuperclass();
		}
		return classes;
	}
}
