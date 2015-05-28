package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public enum PeriodControlType {
	SOFTWARE ("Software: PC controlled"),
	HARDWARE_DAE ("Harware: DAE internal control"),
	HARDWARE_EXTERNAL ("Hardware: External signal control");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (PeriodControlType method : PeriodControlType.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private PeriodControlType(String text) {
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
