package uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels;

import java.util.HashMap;

public enum TimeRegimeMode {
	TCBFILE ("TCB File"),
	DT ("dT = C"),
	DTDIVT ("dT/T = C"),
	DTDIVT2 ("dT/T**2 = C"),
	SHIFTED ("Shifted");
		
	private String mode;
	
	private static HashMap<String, TimeRegimeMode> lookup;
	static {
		lookup = new HashMap<>();
		for (TimeRegimeMode mode : TimeRegimeMode.values()) {
			lookup.put(mode.toString(), mode);
		}
	}
	
	public static TimeRegimeMode fromString(String text) {
		return lookup.containsKey(text) ? lookup.get(text) : TCBFILE;
	}
	
	private TimeRegimeMode(String mode) {
		this.mode = mode;
	}
	
	@Override
	public String toString() {
		return mode;
	}
}
