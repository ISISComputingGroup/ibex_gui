package uk.ac.stfc.isis.ibex.dae.xml;

public class EnumNode<E extends Enum<E>> extends XmlNode<E> {

	private Class<E> enumType;
	
	public EnumNode(String xPathExpression, Class<E> enumType) {
		super(xPathExpression);
		this.enumType = enumType;
	}

	@Override
	public E value() {
		if (node() == null) {
			return null;
		}
		
		int index = Integer.parseInt(node().getTextContent());
		return enumType.getEnumConstants()[index];
	}

	@Override
	public void setValue(E value) {
		if (node() != null) {
			String text = String.format("%d", value.ordinal());
			node().setTextContent(text);
		}
	}


}
