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
import java.util.Arrays;
import java.util.List;

/**
 * Mostly static methods that help coding arrays and <code>List</code>
 * conversion.
 * 
 * @author zemian
 * @version $Id: ArrayUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class ArrayUtils {
	/**
	 * Slice out a array elements using start, end, and step through zero-based
	 * indexes.
	 * 
	 * @param start
	 *            starting index.
	 * @param stop
	 *            stoping index.
	 * @param step
	 *            incremental *@param arrayObj original array object to be
	 *            sliced.
	 * @param src
	 *            Description of the Parameter
	 * @return A copy of (subset of) array according to the slice argument.
	 */
	public static Object[] slice(Object[] src, int start, int stop, int step) {
		if (step <= 1) {
			int size = stop - start + 1;
			Object[] dest = new Object[size];
			System.arraycopy(src, start, dest, 0, size);
			return dest;
		}

		int size = (int) Math.ceil((stop - start + 1) / (double) step);
		Object[] dest = new Object[size];
		int index = 0;
		//System.out.println(size + " "+start + " "+stop + " "+step + " ");
		for (int i = start; i <= stop; i += step) {
			dest[index++] = src[i];
		}

		return dest;
	}

	/**
	 * Slice out a <code>List</code>'s elements using start, end, and step
	 * through zero-based indexes.
	 * 
	 * @param start
	 *            starting index.
	 * @param stop
	 *            stoping index.
	 * @param step
	 *            incremental
	 * @param src
	 *            Description of the Parameter
	 * @return A copy of (subset of) <code>List</code> according to the slice
	 *         argument.
	 */
	public static List slice(List src, int start, int stop, int step) {
		return Arrays.asList(slice(src.toArray(), start, stop, step));
	}

	/**
	 * Shorter verstion of java.lang.System#arraycopy() method, in that we auto
	 * input starting index to be zeros and length of dest's array size
	 * 
	 * @param src
	 *            Description of the Parameter
	 * @return result of array copied object instance.
	 */
	public static Object[] copy(Object[] src) {
		return copy(src, src.length);
	}

	/**
	 * Shorter verstion of java.lang.System#arraycopy() method, in that we auto
	 * input starting index to be zeros
	 * 
	 * @param length
	 *            The amount of element to copy starting from zero index.
	 * @param src
	 *            Description of the Parameter
	 * @return result of array copied object instance.
	 */
	public static Object[] copy(Object[] src, int length) {
		Object[] dest = new Object[length];
		System.arraycopy(src, 0, dest, 0, length);
		return dest;
	}

	/**
	 * Copy the subset of arrays using slice.
	 * 
	 * @param start
	 *            starting index.
	 * @param stop
	 *            stoping index.
	 * @param step
	 *            incremental
	 * @param src
	 *            Description of the Parameter
	 * @return result of array copied object instance.
	 */
	public static Object[] copy(Object[] src, int start, int stop, int step) {
		return slice(src, start, stop, step);
	}

	/**
	 * Copy the subset of <code>List</code> using slice.
	 * 
	 * @param start
	 *            starting index.
	 * @param stop
	 *            stoping index.
	 * @param step
	 *            incremental
	 * @param src
	 *            Description of the Parameter
	 * @return result of array copied object instance.
	 */
	public static List copy(List src, int start, int stop, int step) {
		return slice(src, start, stop, step);
	}

	/**
	 * This metod is same as the List.toArray(new String[0]), but wihtout using
	 * java.lang.reflect to create array, therefore it's faster.
	 * 
	 * @param list
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String[] toStringArray(List list) {
		Object[] array = list.toArray();
		String[] castedArray = new String[array.length];
		System.arraycopy(array, 0, castedArray, 0, array.length);

		return castedArray;
	}

	/**
	 * Converter list to int array.
	 */
	public static int[] toIntArray(List list) {
		List nums = new ArrayList();
		for (int i = 0, maxIndex = list.size(); i < maxIndex; i++) {
			Object obj = list.get(i);
			nums.add(Integer.valueOf(obj.toString()));
		}

		int[] ret = new int[nums.size()];
		for (int i = 0, maxIndex = nums.size(); i < maxIndex; i++) {
			ret[i] = ((Integer) nums.get(i)).intValue();
		}
		return ret;
	}

	/**
	 * Converter int array to a normal list
	 */
	public static List asList(int[] nums) {
		List list = new ArrayList(nums.length);
		for (int i = 0, maxIndex = nums.length; i < maxIndex; i++) {
			list.add(new Integer(nums[i]));
		}

		return list;
	}

	/**
	 * Converter string array to a ArrayList. NOTE that
	 * java.util.Arrays.asList() doesn't return ArrayList object.
	 */
	public static List asList(String[] strs) {
		List list = new ArrayList(strs.length);
		for (int i = 0, maxIndex = strs.length; i < maxIndex; i++) {
			list.add(strs[i]);
		}

		return list;
	}
}
