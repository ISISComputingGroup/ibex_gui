package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class ObservableInstrument implements Instrument {
	
	private final InstrumentDescription instrument;

	private final List<Component> components = new ArrayList<>();
	
	public ObservableInstrument(InstrumentDescription instrument, Variables variables) {
		this.instrument = instrument;
		
		for (ComponentDescription description : instrument.components()) {
			components.add(new ObservableComponent(description, variables));
		} 
	}

	@Override
	public String name() {
		return instrument.name();
	}

	@Override
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	@Override
	public InstrumentDescription getDescription() {
		return instrument;
	}
}
