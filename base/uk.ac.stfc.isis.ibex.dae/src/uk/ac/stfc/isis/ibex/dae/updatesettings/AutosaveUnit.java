package uk.ac.stfc.isis.ibex.dae.updatesettings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum AutosaveUnit {
	FRAMES ("Frames"),
	EVENTS ("Events"),
	DASHBOARD_POLLS ("Dashboard polls"),
	MICROAMPS ("μA");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (AutosaveUnit method : AutosaveUnit.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private AutosaveUnit(String text) {
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
