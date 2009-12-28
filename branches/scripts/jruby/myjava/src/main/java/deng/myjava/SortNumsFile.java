package deng.myjava;

import java.io.*;
import java.util.*;

public class SortNumsFile {
	public static void main(String[] args) {
    List<Integer> nums = readLines(args[0]);
    Collections.sort(nums);
    for (Integer num : nums) {
    	System.out.println(num);
    }
  }
  public static List<Integer> readLines(String filename) {
  	List<Integer> nums = new ArrayList<Integer>();
  	String ln = null;
  	BufferedReader reader = null;
  	try {
  		reader = new BufferedReader(new FileReader(filename));
			while ((ln = reader.readLine()) != null) {
				nums.add(Integer.parseInt(ln));
			}
			return nums;
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			if (reader != null) {
				try { reader.close(); } catch (Exception e) { throw new RuntimeException(e); }
			}
		}
  }
}
