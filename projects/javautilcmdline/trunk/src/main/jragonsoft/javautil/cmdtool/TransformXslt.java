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



package jragonsoft.javautil.cmdtool;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import jragonsoft.javautil.support.GetLongOpt;
import jragonsoft.javautil.support.GetOpt;


/**
 * Transform xml using a xslt file.
 * 
 * @author Zemian Deng
 */
public class TransformXslt {
	/** Program options after it's been parsed. */
	private GetOpt opt;

	private boolean isDebug = false;

	private String filename = "default.txt";

	/** Instantiate main program and parse args then run. */
	public static void main(String[] args) {
		TransformXslt main = new TransformXslt();
		main.opt = new GetLongOpt(args);
		main.run();
	}

	/** Run the main program logic. */
	public void run() {
		if (opt.getArgsCount() != 2 || opt.isOpt("h") || opt.isOpt("help"))
			printExitHelp();

		String xmlFilename = opt.getArg(0);
		String xsltFilename = opt.getArg(1);

		try {
			TransformerFactory factory = TransformerFactory.newInstance();
			Transformer transformer = factory.newTransformer(new StreamSource(
					xsltFilename));
			transformer.transform(new StreamSource(xmlFilename),
					new StreamResult(System.out));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/** Print helppage then exit program. */
	public void printExitHelp() {
		System.out.println("USAGE: TransformXslt [options] xml_file xslt_file");
		System.out.println("  Transform xml using a xslt file.");
		System.out.println("[options]");
		System.out.println("  -h, --help	Display help page.");
		System.out.println("JavaUtil By Zemian Deng.");

		System.exit(0);
	}
}
