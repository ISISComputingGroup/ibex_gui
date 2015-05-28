package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum CalculationMethod {
	UseParametersBelow ("Use parameters below"),
	ReadFromFile ("Read from file");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (CalculationMethod method : CalculationMethod.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private CalculationMethod(String text) {
		this.text = text;
	}
	 
	public static List<String> allToString() {
		return Collections.unmodifiableList(ALLTOSTRING);
	}
	
	@Override
	public String toString() {
		return text;
	}
}
