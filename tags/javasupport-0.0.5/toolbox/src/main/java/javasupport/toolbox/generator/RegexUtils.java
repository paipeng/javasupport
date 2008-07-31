package javasupport.toolbox.generator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public static List<String> findGroupsByDelimeters(String s, String leftSep, String rightSep, String escape){
		List<String> names = new ArrayList<String>();
		String pattern = "(?<=^\\s*|[^("+escape+")])"+leftSep+"(.+?)"+rightSep;
		//System.out.println(">>>"+pattern +" "+s);
		Pattern re = Pattern.compile(pattern);
	    Matcher m = re.matcher(s);
	    while((m.find() && m.groupCount() >=1))
	    	names.add(m.group(1));
		return names;
	}
}
