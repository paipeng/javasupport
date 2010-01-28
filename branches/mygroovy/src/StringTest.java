import java.util.Scanner;

import org.junit.Test;


public class StringTest {
	@Test
	public void testScanner() {
		Scanner s = new Scanner("a b c d e f g").useDelimiter(" ");
		while(s.hasNext())
			System.out.println(s.next());
	}
	
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
}
