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

package jragonsoft.javautil.conf;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

import jragonsoft.javautil.support.EasyMap;
import jragonsoft.javautil.util.ExceptionUtils;
import jragonsoft.javautil.util.XmlUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;


/**
 * This config map will load a xml file that forms a tree map of all string
 * properties. The name of xml elements within the same level must be uqnique,
 * as it's going to be used as key name into the map. Nest xml tree element are
 * formed a nested Map in this config map.
 * <p>
 * 
 * Sample file <br>
 * 
 * <pre>
 * 
 * &lt;top&gt;
 * &lt;deep&gt;
 * &lt;price&gt;55&lt;/price&gt;
 * &lt;msg&gt;xml configuration file is cool.&lt;/msg&gt;
 * &lt;/deep&gt;
 * &lt;/top&gt;
 * 
 * </pre>
 * 
 * <p>
 * 
 * Variable expansion only works within each section.
 * 
 * @author zemian
 * @version $Id: XmlProperties.java 42 2006-05-10 23:25:49Z zemian $
 * @see ConfigProperties
 */
public class XmlProperties extends EasyMap {
	/**
	 *  
	 */
	private static final long serialVersionUID = 5831261332078949849L;

	private boolean isUseId = false;

	/**
	 * Sets the useId attribute of the XmlProperties object
	 * 
	 * @param flag
	 *            The new useId value
	 */
	public void setUseId(boolean flag) {
		isUseId = flag;
	}

	/**
	 * Description of the Method
	 * 
	 * @param parentMap
	 *            Description of the Parameter
	 * @param element
	 *            Description of the Parameter
	 */
	public void loadNodeToMap(EasyMap parentMap, Element element) {
		//System.out.println(node.getNodeName() + " "+
		// XmlUtils.getNodeTypeName(node));

		//get map's name
		String name = element.getNodeName();
		if (isUseId) {
			name = element.getAttribute("id");
		}

		//insert text or nested map.
		Element[] children = XmlUtils.getElementNodes(element);
		if (children.length == 0) {
			Text text = XmlUtils.getFirstTextNode(element);
			if (text == null) {
				//throw new RuntimeException("Element " + name + " has no Text
				// child!");
				parentMap.put(name, "");
			} else {
				parentMap.put(name, text.getNodeValue());
			}
		} else {
			EasyMap nestedMap = new EasyMap();
			parentMap.put(name, nestedMap);
			parentMap = nestedMap;
		}

		for (int i = 0, maxIndex = children.length; i < maxIndex; i++) {
			loadNodeToMap(parentMap, children[i]);
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param inStream
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void load(InputStream inStream) throws IOException {
		try {
			Document xmlDocument = XmlUtils.open(inStream);
			Element root = xmlDocument.getDocumentElement();
			loadNodeToMap(this, root);
		} catch (Exception e) {
			throw new IOException("Can't load xml properties file. "
					+ ExceptionUtils.getLastStackTrace(e));
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @param outStream
	 *            Description of the Parameter
	 * @param header
	 *            Description of the Parameter
	 * @exception IOException
	 *                Description of the Exception
	 */
	public void store(OutputStream outStream, String header) throws IOException {
		PrintWriter out = new PrintWriter(new BufferedWriter(
				new OutputStreamWriter(outStream)));
		out.println("<!-- Created On: " + new Date() + "  -->");
		if (header != null) {
			out.println("<!-- " + header + " -->");
		}
		out.println();
		store(out, this);
		out.flush();
	}

	/**
	 * Description of the Method
	 * 
	 * @param out
	 *            Description of the Parameter
	 * @param map
	 *            Description of the Parameter
	 */
	private void store(PrintWriter out, EasyMap map) {
		String[] names = (String[])map.keySet().toArray();
		for (int i = 0, maxIndex = names.length; i < maxIndex; i++) {
			String name = names[i];
			Object value = map.get(name);
			if (value instanceof EasyMap) {
				out.println("<" + name + ">");
				store(out, (EasyMap) value);
				out.println("</" + name + ">");
			} else if (value != null) {
				out.println("<" + name + ">" + value.toString() + "</" + name
						+ ">");
			}
		}
	}

	/**
	 * Description of the Method
	 * 
	 * @return Description of the Return Value
	 */
	public String toString() {
		StringWriter writer = new StringWriter();
		PrintWriter out = new PrintWriter(new BufferedWriter(writer));
		out.println();
		store(out, this);
		out.flush();
		return writer.getBuffer().toString();
	}
}