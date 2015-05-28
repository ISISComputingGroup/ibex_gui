package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public enum PeriodType {
	UNUSED ("Unused"),
	DAQ ("DAQ"),
	DWELL ("Dwell");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (PeriodType method : PeriodType.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private PeriodType(String text) {
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
