package uk.ac.stfc.isis.ibex.dae.xml;

public class StringNode extends XmlNode<String> {

	public StringNode(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	public String value() {
		if (node() == null) {
			return null;
		}
		
		return node().getTextContent();
	}

	@Override
	public void setValue(String value) {
		if (node() != null) {
			node().setTextContent(value);		
		}
	}
}
