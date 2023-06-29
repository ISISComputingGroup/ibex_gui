package uk.ac.stfc.isis.ibex.configserver;

import java.util.HashMap;

public class MoxaMappings {
	private HashMap<String, String> namesToIps; 
	private HashMap<String, HashMap<String, String>> namesToPorts;
	
	public HashMap<String, HashMap<String, String>> getMappings() {
		return namesToPorts;
	}
	public MoxaMappings(MoxaMappings other) {
		namesToIps = other.namesToIps;
		namesToPorts = other.namesToPorts;
	}
	public MoxaMappings(HashMap<String, String> namesToIps, HashMap<String, HashMap<String, String>> namesToPorts) {
		this.namesToIps = namesToIps;
		this.namesToPorts = namesToPorts;
	}
}
