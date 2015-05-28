package uk.ac.stfc.isis.ibex.experimentdetails.internal;

import uk.ac.stfc.isis.ibex.experimentdetails.Parameter;

public class UnknownParameter extends Parameter {
	@Override
	public String getName() {
		return "Unknown";
	}
	
	@Override
	public String getUnits() {
		return "";
	}
	
	@Override
	public String getValue() {
		return "";
	}
}
