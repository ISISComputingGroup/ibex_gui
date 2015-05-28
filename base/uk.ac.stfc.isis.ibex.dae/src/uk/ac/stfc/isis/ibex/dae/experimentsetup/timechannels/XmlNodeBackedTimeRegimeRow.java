package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import org.w3c.dom.Node;

public class XmlNodeBackedTimeRegimeRow extends TimeRegimeRow {

	private Node from;
	private Node to;
	private Node step;
	private Node mode;
	
	public XmlNodeBackedTimeRegimeRow(Node from, Node to, Node step, Node mode) {
		this.from = from;
		this.to = to;
		this.step = step;
		this.mode = mode;
		
		super.setFrom(Double.parseDouble(from.getTextContent()));
		super.setTo(Double.parseDouble(to.getTextContent()));
		super.setStep(Double.parseDouble(step.getTextContent()));
		
		int index = Integer.parseInt(mode.getTextContent());
		super.setMode(TimeRegimeMode.values()[index]);
	}
	
	@Override
	public void setFrom(double value) {
		super.setFrom(value);		
		setDouble(from, value);
	}
	
	@Override
	public void setTo(double value) {
		super.setTo(value);
		setDouble(to, value);
	}
	
	@Override
	public void setStep(double value) {
		super.setStep(value);
		setDouble(step, value);
	}
	
	@Override
	public void setMode(TimeRegimeMode value) {
		super.setMode(value);
		setInt(mode, value.ordinal());
	}
	
	private static void setDouble(Node node, double value) {
		node.setTextContent(String.format("%f", value));
	}
	
	private static void setInt(Node node, int value) {
		node.setTextContent(String.format("%d", value));
	}
}
