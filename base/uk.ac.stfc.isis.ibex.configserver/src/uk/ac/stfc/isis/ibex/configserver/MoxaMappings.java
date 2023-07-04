package uk.ac.stfc.isis.ibex.configserver;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;

public class MoxaMappings {
	private HashMap<String, String> namesToIps; 
	private HashMap<String, HashMap<String, String>> namesToPorts;
	private UpdatedObservableAdapter<MoxaMappings> moxas;
	
	public HashMap<String, HashMap<String, String>> getMappings() {
		return namesToPorts;
	}
	public MoxaMappings(MoxaMappings other) {
		namesToIps = other.namesToIps;
		namesToPorts = other.namesToPorts;
	}
	public MoxaMappings(ConfigServer server, HashMap<String, String> namesToIps, HashMap<String, HashMap<String, String>> namesToPorts) {
		this.moxas = new UpdatedObservableAdapter<>(server.moxaMappings());
		this.namesToIps = namesToIps;
		this.namesToPorts = namesToPorts;
	}
}
