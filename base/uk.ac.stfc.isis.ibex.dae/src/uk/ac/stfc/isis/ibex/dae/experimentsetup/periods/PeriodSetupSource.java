package uk.ac.stfc.isis.ibex.dae.experimentsetup.periods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public enum PeriodSetupSource {
	PARAMETERS ("Use parameters below"),
	FILE ("Read from file");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (PeriodSetupSource method : PeriodSetupSource.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private PeriodSetupSource(String text) {
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
