package uk.ac.stfc.isis.ibex.configserver;

import java.util.ArrayList;
import java.util.HashMap;

import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MoxaMappings {
	private UpdatedObservableAdapter<HashMap<String, ArrayList<ArrayList<String>>>> moxas;
	
	public UpdatedValue<HashMap<String, ArrayList<ArrayList<String>>>> getMappings() {
		return moxas;
	}
	
	public MoxaMappings(ConfigServer server) {
		this.moxas = new UpdatedObservableAdapter<>(server.moxaMappings());
	}
}
