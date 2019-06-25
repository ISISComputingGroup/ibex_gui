package uk.ac.stfc.isis.ibex.scriptgenerator;

import uk.ac.stfc.isis.ibex.model.ModelObject;

public class ToyModel extends ModelObject {
	private Integer iteratedNumber = 0;
	private String iteratedNumberString;
	
	public int iterateNumber(){
		iteratedNumber++;
		setIteratedNumber(iteratedNumber.toString());
		return iteratedNumber;
	}
	
	public String getIteratedNumber() {
		
		return iteratedNumberString;
	}
	
	protected synchronized void setIteratedNumber(String newValue) {
		firePropertyChange("iteratedNumber", this.iteratedNumberString, this.iteratedNumberString = newValue);
	}
}
