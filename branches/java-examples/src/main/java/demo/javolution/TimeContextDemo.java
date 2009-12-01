package demo.javolution;

import java.util.ArrayList;

import javolution.testing.TimeContext;

public class TimeContextDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TimeContext.enter();		
		ArrayList<Integer> ls = new ArrayList<Integer>();
		for (int i = 0; i < 100000; i++) {
			ls.add(i);
		}						
		System.out.println(TimeContext.getAverageTime("ms"));
		TimeContext.exit();
	}

}
