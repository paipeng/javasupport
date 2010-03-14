
class StringUtils {
	def List splitStr(String input, String sep) { 
		input.split(sep).toList() 
	}
	
	def List splitStr2(String input, String sep) { 
		def ret = []
		def scan = new Scanner(input).useDelimiter(sep);
		while (scan.hasNext()) {
			ret << scan.next()
		}
		ret
	}
	
	public List splitStr3(String source, String delim) {
		int maxNumSplit = -1;
		final int SIZE = 10;
		int index = 0;
		String[] result = [];

		if (delim == null || delim.length() == 0) {
			return [ source ];
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
