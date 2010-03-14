package ztool;

import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import ztool.CliHelper.Options;
import ztool.CliHelper.OptionsParser;

// http://www.esvapi.org/api
public class Bible extends CliBase {
	@Override
	public void initOptionsParser(OptionsParser optsParser) {
		optsParser
				.setSummary("Display Words of God that can save your soul!\n\n" +
						"The Bible quotes are provided by http://www.esvapi.org\n")
				.setUsage("ztool Bible [Options] [passageQuery ...]\n")
				.setExamples(
					  "  ztool Bible\n"
					+ "  ztool Bible John 3:16\n"
					+ "  ztool Bible 2 Timothy 3:16,17\n"
					+ "  ztool Bible Jn 6:35,  8:12, 10:9, 11:25, 14:6, 15:5\n"
					+ "  ztool Bible Romans 3:10, 3:23, 5:12, 6:23, 5:8, 10:9-10, 10:13\n"
					+ "");
	}

	@Override
	public void run(Options opts) {
		String input = "";
		for (String arg : opts.getArgs()) {
			input += arg + " ";
		}

		if (input.trim().equals("")) {
			input = "";
		}
		debug("Search input: " + input);
		
		try {
			String passage = getPassage(input);
			System.out.println(passage);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public String getPassage(String search) throws Exception {
		StringBuilder urlStringBuilder = new StringBuilder();
		urlStringBuilder.append("http://www.esvapi.org/v2/rest/verse");
		urlStringBuilder.append("?key=IP");
		urlStringBuilder.append("&include-headings=false");
		urlStringBuilder.append("&include-footnotes=false");
		urlStringBuilder.append("&output-format=plain-text");
		if (!search.equals("")) {
			urlStringBuilder.append("&passage="
				+ URLEncoder.encode(search, "ISO-8859-1"));
		}
		URL esvURL = new URL(urlStringBuilder.toString());
		InputStream esvStream = esvURL.openStream();

		StringBuilder outStringBuilder = new StringBuilder();
		int nextChar;

		while ((nextChar = esvStream.read()) != -1) {
			outStringBuilder.append((char) nextChar);
		}

		esvStream.close();

		return outStringBuilder.toString();
	}

}
