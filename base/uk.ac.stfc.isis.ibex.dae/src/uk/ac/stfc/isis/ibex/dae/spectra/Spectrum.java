package uk.ac.stfc.isis.ibex.dae.spectra;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class Spectrum extends ModelObject {

	private int number;
	private int period;
	private double[] xData = new double[2];
	private double[] yData = new double[2];
	
	public int getNumber() {
		return number;
	}
	
	public void setNumber(int value) {
		firePropertyChange("number", number, number = value);
	}
	
	public int getPeriod() {
		return period;
	}
	
	public void setPeriod(int value) {
		firePropertyChange("period", period, period = value);
	}
	
	public double[] xData() {
		return xData;
	}
		
	public double[] yData() {
		return yData;
	}
	
	protected void setXData(double[] value) {
		firePropertyChange("xData", xData, xData = value);
	}
	
	protected void setYData(double[] value) {
		firePropertyChange("yData", yData, yData = value);
	}
}
