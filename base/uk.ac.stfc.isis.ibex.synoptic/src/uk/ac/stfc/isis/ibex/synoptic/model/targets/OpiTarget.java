package uk.ac.stfc.isis.ibex.synoptic.model.targets;

import uk.ac.stfc.isis.ibex.synoptic.model.Target;

public class OpiTarget extends Target {
	
	private final String opiName;
	
	public OpiTarget(String name, String opiName) {
		super(name);
		this.opiName = opiName;
	}
	
	public String opiName() {
		return opiName;
	}
}
