package uk.ac.stfc.isis.ibex.dae.dataacquisition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum DaeTimingSource {
	ISIS ("ISIS"),
	INTERNAL_TEST_CLOCK ("Internal Test Clock"),
	SMP ("SMP"),
	MUON_CERENKOV ("Muon Cerenkov"),
	MUON_MS ("Muon MS"),
	ISIS_FIRST_TS1 ("ISIS (first TS1)");

	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (DaeTimingSource method : DaeTimingSource.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private DaeTimingSource(String text) {
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
