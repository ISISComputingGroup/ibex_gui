package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum TimeUnit {
	MICROSECONDS ("Microseconds"),
	NANOSECONDS ("Nanoseconds");

	private String text;
	
	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (TimeUnit method : TimeUnit.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private TimeUnit(String text) {
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
