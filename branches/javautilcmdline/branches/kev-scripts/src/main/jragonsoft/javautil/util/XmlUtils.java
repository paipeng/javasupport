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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * return a string name of a given node instance.
 * 
 * @author zemian
 * @version $Id: XmlUtils.java 42 2006-05-10 23:25:49Z zemian $
 */
public class XmlUtils {
	public static String getNodeTypeName(Node node) {
		short type = node.getNodeType();
		if (type == Node.ATTRIBUTE_NODE)
			return "ATTRIBUTE_NODE";
		else if (type == Node.CDATA_SECTION_NODE)
			return "CDATA_SECTION_NODE";
		else if (type == Node.COMMENT_NODE)
			return "COMMENT_NODE";
		else if (type == Node.DOCUMENT_FRAGMENT_NODE)
			return "DOCUMENT_FRAGMENT_NODE";
		else if (type == Node.DOCUMENT_NODE)
			return "DOCUMENT_NODE";
		else if (type == Node.DOCUMENT_TYPE_NODE)
			return "DOCUMENT_TYPE_NODE";
		else if (type == Node.ELEMENT_NODE)
			return "ELEMENT_NODE";
		else if (type == Node.ENTITY_NODE)
			return "ENTITY_NODE";
		else if (type == Node.ENTITY_REFERENCE_NODE)
			return "ENTITY_REFERENCE_NODE";
		else if (type == Node.NOTATION_NODE)
			return "NOTATION_NODE";
		else if (type == Node.PROCESSING_INSTRUCTION_NODE)
			return "PROCESSING_INSTRUCTION_NODE";
		else if (type == Node.TEXT_NODE)
			return "TEXT_NODE";
		else
			return "UNKOWN_NODE";
	}

	/**
	 * Convert a flat records into xml well formed content.
	 * 
	 * @param input
	 *            Description of the Parameter
	 * @param recordSep
	 *            Description of the Parameter
	 * @param fieldSep
	 *            Description of the Parameter
	 * @param trimFieldValue
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 */
	public static String flatRecordToXml(String input, String recordSep,
			String fieldSep, boolean trimFieldValue) {
		String lineSep = FileUtils.LINE_SEP;
		StringBuffer xml = new StringBuffer();
		xml.append("<Table>" + lineSep);
		String[] lines = StringUtils.split(input, recordSep);
		for (int j = 0, maxLines = lines.length; j < maxLines; j++) {
			String line = lines[j];
			String[] fields = StringUtils.split(line, fieldSep);

			xml.append("  <Record>" + lineSep);
			for (int k = 0, maxField = fields.length; k < maxField; k++) {
				String field = fields[k];
				if (trimFieldValue) {
					field = field.trim();
				}
				xml.append("    <Field>" + escapeXmlCharaters(field)
						+ "</Field>" + lineSep);
			}
			xml.append("  </Record>" + lineSep);
		}
		xml.append("</Table>" + lineSep);

		return xml.toString();
	}

	/**
	 * Return escaped XML text value with these occurances: lt, gt, amp, apos,
	 * quot &#38;#60;, &#62;, &#38;#38;, &#39;, &#34;
	 */
	public static String escapeXmlCharaters(String value) {
		value = value.replaceAll("<", "&lt;");
		value = value.replaceAll(">", "&gt;");
		value = value.replaceAll("&", "&amp;");
		value = value.replaceAll("'", "&apos;");
		value = value.replaceAll("\"", "&quot;");
		return value;
	}

	/**
	 * Open a xml file by filename and return Document object, which is an Node
	 * 
	 * @param uri
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception ParserConfigurationException
	 *                Description of the Exception
	 * @exception SAXException
	 *                Description of the Exception
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static Document open(String uri)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = fac.newDocumentBuilder();
		return docBuilder.parse(new InputSource(uri));
	}
	
	/* Open a xml File and return Document object, which is an Node */
	public static Document open(File file)
		throws ParserConfigurationException, SAXException, IOException {
		return open(new FileInputStream(file));
	}

	/**
	 * Open a xml input stream and return Document object, which is an Node
	 * 
	 * @param in
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception ParserConfigurationException
	 *                Description of the Exception
	 * @exception SAXException
	 *                Description of the Exception
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static Document open(InputStream in)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = fac.newDocumentBuilder();
		return docBuilder.parse(new InputSource(in));
	}

	/**
	 * Open a xml reader and return Document object, which is an Node
	 * 
	 * @param reader
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception ParserConfigurationException
	 *                Description of the Exception
	 * @exception SAXException
	 *                Description of the Exception
	 * @exception IOException
	 *                Description of the Exception
	 */
	public static Document open(Reader reader)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fac = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = fac.newDocumentBuilder();
		return docBuilder.parse(new InputSource(reader));
	}

	/**
	 * Decide if the node is text, make sure the Node.getNodeValue will return
	 * some value.
	 * 
	 * @param node
	 *            Description of the Parameter
	 * @return The textNode value
	 */
	public static boolean isTextNode(Node node) {
		if (node == null) {
			return false;
		} else if (node instanceof Text || node instanceof CDATASection
				|| node instanceof Attr || node instanceof Comment
				|| node instanceof ProcessingInstruction) {
			return true;
		}

		return false;
	}

	/**
	 * A failed safe method to retrieve text from a node
	 * 
	 * @param node
	 *            Description of the Parameter
	 * @return The text value
	 */
	public static String getTextNodeValue(Node node) {
		if (isTextNode(node)) {
			return node.getNodeValue();
		}

		return "";
	}

	/** return the first found Text node of a given node. */
	public static Text getFirstTextNode(Node node) {
		NodeList children = node.getChildNodes();
		for (int i = 0, maxIndex = children.getLength(); i < maxIndex; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.TEXT_NODE) {
				return (Text) child;
			}
		}

		return null;
	}

	/** return first level of child Elements in a given node. */
	public static Element[] getElementNodes(Node node) {
		NodeList children = node.getChildNodes();
		List elements = new ArrayList();
		for (int i = 0, maxIndex = children.getLength(); i < maxIndex; i++) {
			Node child = children.item(i);
			if (child.getNodeType() == Node.ELEMENT_NODE) {
				elements.add((Element) child);
			}
		}

		return (Element[]) elements.toArray(new Element[0]);
	}

	/**
	 * Flatten the complete node tree into a string.
	 * 
	 * @param node
	 *            Description of the Parameter
	 * @return Description of the Return Value
	 * @exception TransformerConfigurationException
	 *                Description of the Exception
	 * @exception TransformerException
	 *                Description of the Exception
	 */
	public static String flattenNode(Node node)
			throws TransformerConfigurationException, TransformerException {
		TransformerFactory fac = TransformerFactory.newInstance();
		Transformer serializer = fac.newTransformer();
		serializer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		serializer.setOutputProperty(OutputKeys.INDENT, "yes");
		StringWriter writer = new StringWriter();
		serializer.transform(new DOMSource(node), new StreamResult(writer));
		return writer.getBuffer().toString();
	}
}