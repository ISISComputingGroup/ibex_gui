package uk.ac.stfc.isis.ibex.dae.xml;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class XmlNode<T> {

	private final XPath xpath = XPathFactory.newInstance().newXPath();
	private final String expression;
	
	private Node node;
	
	public XmlNode(String xPathExpression) {
		expression = xPathExpression;
	}
	
	public void setDoc(Document doc) throws XPathExpressionException {
		setNode(doc);
	}

	public abstract T value();
	
	public abstract void setValue(T value);
	
	protected Node node() {
		return node;
	}
	
	private void setNode(Document doc) throws XPathExpressionException {
		NodeList nodes = (NodeList) xpath.compile(expression).evaluate(doc, XPathConstants.NODESET);	
		node =  nodes.item(0);
	}
}
