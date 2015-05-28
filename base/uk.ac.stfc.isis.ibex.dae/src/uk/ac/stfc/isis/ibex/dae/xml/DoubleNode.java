package uk.ac.stfc.isis.ibex.dae.xml;

public class DoubleNode extends XmlNode<Double> {

	public DoubleNode(String xPathExpression) {
		super(xPathExpression);
	}

	@Override
	public Double value() {
		if (node() == null) {
			return null;
		}
		
		return Double.parseDouble(node().getTextContent());
	}

	@Override
	public void setValue(Double value) {
		if (node() != null) {
			node().setTextContent(String.format("%f", value));		
		}
	}
}
