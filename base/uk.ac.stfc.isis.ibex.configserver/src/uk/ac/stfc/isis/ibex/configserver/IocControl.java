package uk.ac.stfc.isis.ibex.configserver;

import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.ConfigServer;
import uk.ac.stfc.isis.ibex.configserver.EditableIocState;
import uk.ac.stfc.isis.ibex.epics.adapters.UpdatedObservableAdapter;
import uk.ac.stfc.isis.ibex.model.SetCommand;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class IocControl {

	private final UpdatedObservableAdapter<Collection<EditableIocState>> iocs;
	private final SetCommand<Collection<String>> start;
	private final SetCommand<Collection<String>> stop;
	private final SetCommand<Collection<String>> restart;

	public IocControl(ConfigServer server) {
		this.iocs = new UpdatedObservableAdapter<>(server.iocStates());
		
		start = server.startIoc();
		stop = server.stopIoc();
		restart = server.restartIoc();
	}
	
	public UpdatedValue<Collection<EditableIocState>> iocs() {
		return iocs;
	}
	
	public SetCommand<Collection<String>> startIoc() {
		return start;
	}

	public SetCommand<Collection<String>> stopIoc() {
		return stop;
	}
	
	public SetCommand<Collection<String>> restartIoc() {
		return restart;
	}
}
