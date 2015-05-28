package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class TimeRegimeRow extends ModelObject {
	
	private double from;
	private double to;
	private double step;
	private TimeRegimeMode mode = TimeRegimeMode.TCBFILE;
	
	public double getFrom() {
		return from;
	}
	
	public double getTo() {
		return to;
	}
	
	public double getStep() {
		return step;
	}
	
	public TimeRegimeMode getMode() {
		return mode;
	}
	
	public void setFrom(double value) {
		firePropertyChange("from", from, from = value);
	}
	
	public void setTo(double value) {
		firePropertyChange("to", to, to = value);
	}
	
	public void setStep(double value) {
		firePropertyChange("step", step, step = value);
	}
	
	public void setMode(TimeRegimeMode value) {
		firePropertyChange("mode", mode, mode = value);
	}
}
