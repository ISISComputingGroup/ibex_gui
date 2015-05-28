package uk.ac.stfc.isis.ibex.dae.xml;

public class IntegerNode extends XmlNode<Integer> {

	public IntegerNode(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	public Integer value() {
		if (node() == null) {
			return null;
		}
		
		return Integer.parseInt(node().getTextContent());
	}

	@Override
	public void setValue(Integer value) {
		if (node() != null) {
			node().setTextContent(String.format("%d", value));		
		}
	}
}
