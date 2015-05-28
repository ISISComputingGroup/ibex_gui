package uk.ac.stfc.isis.ibex.synoptic;

import java.util.Collection;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.epics.writing.Writable;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.internal.ObservableInstrument;
import uk.ac.stfc.isis.ibex.synoptic.internal.Variables;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.navigation.InstrumentNavigationGraph;

public class SynopticModel extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger("SynopticModel");
	
	private final Variables variables;

	private ObservableInstrument instrument; 
	private SynopticWriter setCurrentSynoptic;
	
	public SynopticModel(Variables variables) {
		this.variables = variables;
		instrument = getInstrument(new InstrumentDescription());
		
		setCurrentSynoptic = new SynopticWriter(variables.setSynoptic);
	}

	public Instrument instrument() {
		return instrument;
	}
	
	public Writable<Collection<String>> deleteSynoptics() {
		return variables.deleteSynoptics;
	}
	
	public SynopticWriter saveSynoptic() {
		return setCurrentSynoptic;
	}
	
	public void setInstrumentFromDescription(InstrumentDescription description) {
		firePropertyChange("instrument", instrument, instrument = getInstrument(description));
	}
	
	public InstrumentNavigationGraph instrumentGraph() {
		return new InstrumentNavigationGraph(instrument);
	}
		
	protected Logger logger() {
		return LOG;
	}
	
	private ObservableInstrument getInstrument(InstrumentDescription description) {
		return new ObservableInstrument(description, variables);
	}
}
