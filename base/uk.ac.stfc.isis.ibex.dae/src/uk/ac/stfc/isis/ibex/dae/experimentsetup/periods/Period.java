package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Period extends ModelObject {

	private int number;
	private PeriodType type = PeriodType.UNUSED;
	private int frames;
	private int binaryOutput;
	private String label = "";
	
	public Period(int number) {
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	public PeriodType getType() {
		return type;
	}
	
	public void setType(PeriodType value) {
		firePropertyChange("type", type, type = value);
	}

	public int getFrames() {
		return frames;
	}

	public void setFrames(int value) {
		firePropertyChange("frames", frames, frames = value);
	}

	public int getBinaryOutput() {
		return binaryOutput;
	}

	public void setBinaryOutput(int value) {
		firePropertyChange("binaryOutput", binaryOutput, binaryOutput = value);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String value) {
		firePropertyChange("label", label, label = value);
	}
}
