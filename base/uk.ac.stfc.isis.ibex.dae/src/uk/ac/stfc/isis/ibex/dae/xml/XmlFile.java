package uk.ac.stfc.isis.ibex.dae.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlFile {
	
	private final List<XmlNode<?>> nodes;
	private Document doc;

	public XmlFile(List<XmlNode<?>> nodes) {
		this.nodes = nodes;
	}
	
	public void setXml(String xml) {
		try {
			buildDocument(xml);
			setNodes();
		} catch (ParserConfigurationException | SAXException | IOException | XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		if (doc == null) {
			return "";
		}
		
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer;
		try {
			transformer = tf.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			StringWriter writer = new StringWriter();
			transformer.transform(new DOMSource(doc), new StreamResult(writer));
			String output = writer.getBuffer().toString();
			
			return output;
		} catch (TransformerException e) {
			return "";
		}
	}	
	
	private void buildDocument(String xml) throws ParserConfigurationException, SAXException, IOException {
	    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder builder = factory.newDocumentBuilder();    
	    InputSource source = new InputSource(new StringReader(xml));
	    
	    doc = builder.parse(source);
	}
	
	private void setNodes() throws XPathExpressionException {
		for (XmlNode<?> node : nodes) {
			node.setDoc(doc);
		}
	}
}
