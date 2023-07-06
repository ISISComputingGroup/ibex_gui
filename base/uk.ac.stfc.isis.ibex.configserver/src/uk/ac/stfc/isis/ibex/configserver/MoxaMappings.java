package uk.ac.stfc.isis.ibex.configserver;

import java.util.HashMap;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MoxaMappings {
//	private HashMap<String, String> namesToIps; 
//	private HashMap<String, HashMap<String, String>> namesToPorts;
	private UpdatedObservableAdapter<HashMap<String, HashMap<String, String>>> moxas;
	
	public UpdatedValue<HashMap<String, HashMap<String, String>>> getMappings() {
		return moxas;
	}
//	public MoxaMappings(MoxaMappings other) {
//		namesToIps = other.namesToIps;
//		namesToPorts = other.namesToPorts;
//	}
//	public MoxaMappings(ConfigServer server, HashMap<String, String> namesToIps, HashMap<String, HashMap<String, String>> namesToPorts) {
//		this.moxas = new UpdatedObservableAdapter<>(server.moxaMappings());
//		this.namesToIps = namesToIps;
//		this.namesToPorts = namesToPorts;
//	}
	public MoxaMappings(ConfigServer server) {
		this.moxas = new UpdatedObservableAdapter<>(server.moxaMappings());
		
		
//		this.namesToIps = new HashMap<String, String>();
//		this.namesToPorts = new HashMap<String, HashMap<String,String>>();
		//this.namesToPorts = server.moxaMappings();
	}
}
