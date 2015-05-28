package uk.ac.stfc.isis.ibex.synoptic.model;

import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public interface Instrument {
	
	String name();
	
	List<? extends Component> components();
	
	InstrumentDescription getDescription();	
}
