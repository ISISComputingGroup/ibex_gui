package uk.ac.stfc.isis.ibex.dae.dataacquisition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public enum BinaryChoice {
	NO ("No"),
	YES ("Yes");
	
	private String text;

	private static final List<String> ALLTOSTRING;
	static {
		ALLTOSTRING = new ArrayList<>();
		for (BinaryChoice method : BinaryChoice.values()) {
			ALLTOSTRING.add(method.toString());
		}
	}
	
	private BinaryChoice(String text) {
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
