package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

/**
 * This class is a wrapper around the DOM xml parser and provides methods that make parsing easier.
 * @author Tristan Bepler
 *
 */
public class XMLParser {
	
	Document doc;
	
	public XMLParser(String xmlFile) throws SAXException, IOException{
		DOMParser parser = new DOMParser();
		parser.parse(xmlFile);
		doc = parser.getDocument();
	}
	
	/**
	 * This method returns a collection of string arrays, where each array represents a node with the specified nodeTag and
	 * each string in the array corresponds to the value of that attribute in the specified attributes array.
	 * @param nodeTag - the tag of the nodes to be searched
	 * @param attributes - a string array containing the attribute tags to be searched
	 * @return a collection of string arrays containing the attribute values for each node found
	 */
	public List<String[]> getAttributesFromNodes(String nodeTag, String[] attributes){
		List<String[]> results = new ArrayList<String[]>();
		NodeList nodes = doc.getElementsByTagName(nodeTag);
		for(int i=0; i<nodes.getLength(); i++){
			Element e = (Element) nodes.item(i);
			String[] vals = new String[attributes.length];
			for(int j=0; j<vals.length; j++){
				vals[j] = e.getAttribute(attributes[j]);
			}
			results.add(vals);
		}
		return results;
	}
	
	public List<String> getTextContentsFromNodes(String nodeTag){
		List<String> results = new ArrayList<String>();
		NodeList nodes = doc.getElementsByTagName(nodeTag);
		for(int i=0; i<nodes.getLength(); i++){
			Element e = (Element) nodes.item(i);
			results.add(e.getTextContent().trim());
		}
		return results;
	}
	
	public List<String[]> parseDataTypes(){
		List<String[]> results = new ArrayList<String[]>();
		NodeList nodes = doc.getElementsByTagName("datatype");
		for(int i=0; i<nodes.getLength(); i++){
			Element e = (Element) nodes.item(i);
			String[] entry = new String[4];
			entry[0] = e.getAttribute("id");
			entry[1] = e.getAttribute("classpath");
			entry[2] = e.getElementsByTagName("reader").item(0).getTextContent().trim();
			entry[3] = e.getElementsByTagName("writer").item(0).getTextContent().trim();
			results.add(entry);
		}
		return results;
	}
	
	public List<String[]> parseModules(){
		List<String[]> results = new ArrayList<String[]>();
		NodeList nodes = doc.getElementsByTagName("module");
		for(int i=0; i<nodes.getLength(); i++){
			Element e = (Element) nodes.item(i);
			String entry[] = new String[3];
			entry[0] = e.getAttribute("name");
			entry[1] = e.getAttribute("classpath");
			entry[2] = e.getElementsByTagName("description").item(0).getTextContent().trim();
			results.add(entry);
		}
		return results;
	}
	
	
	
	
	
	
}
