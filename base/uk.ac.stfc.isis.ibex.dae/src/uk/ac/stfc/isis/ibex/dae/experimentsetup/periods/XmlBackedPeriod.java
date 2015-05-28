package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.dae.xml.EnumNode;
import uk.ac.stfc.isis.ibex.dae.xml.IntegerNode;
import uk.ac.stfc.isis.ibex.dae.xml.StringNode;
import uk.ac.stfc.isis.ibex.dae.xml.XmlNode;

public class XmlBackedPeriod extends Period {
	
	private static final String TYPE_EXPRESSION_FORMAT = "/Cluster/EW[Name='Type %d']/Val";
	private static final String FRAMES_EXPRESSION_FORMAT = "/Cluster/I32[Name='Frames %d']/Val";
	private static final String BINARY_OUTPUT_EXPRESSION_FORMAT = "/Cluster/U16[Name='Output %d']/Val";
	private static final String LABEL_EXPRESSION_FORMAT = "/Cluster/String[Name='Label %d']/Val";
	
	private final EnumNode<PeriodType> type;
	private final IntegerNode frames;
	private final IntegerNode binaryOutput;
	private final StringNode label;
	
	private final List<XmlNode<?>> nodes = new ArrayList<>();
	
	public XmlBackedPeriod(int number) {
		super(number);
		type = new EnumNode<>(String.format(TYPE_EXPRESSION_FORMAT, number), PeriodType.class);
		frames = new IntegerNode(String.format(FRAMES_EXPRESSION_FORMAT, number));
		binaryOutput = new IntegerNode(String.format(BINARY_OUTPUT_EXPRESSION_FORMAT, number));
		label = new StringNode(String.format(LABEL_EXPRESSION_FORMAT,	number));
		
		nodes.add(type);
		nodes.add(frames);
		nodes.add(binaryOutput);
		nodes.add(label);
	}
	
	public List<XmlNode<?>> nodes() {
		return nodes;
	}
	
	public void initialiseFromNodes() {
		super.setType(type.value());
		super.setFrames(frames.value());
		super.setBinaryOutput(binaryOutput.value());
		super.setLabel(label.value());
	}
	
	@Override
	public void setType(PeriodType value) {
		super.setType(value);
		type.setValue(value);
	}
	
	@Override
	public void setFrames(int value) {
		super.setFrames(value);
		frames.setValue(value);
	}
	
	@Override
	public void setBinaryOutput(int value) {
		super.setBinaryOutput(value);
		binaryOutput.setValue(value);
	}
	
	@Override
	public void setLabel(String value) {
		super.setLabel(value);
		label.setValue(value);
	}
}
