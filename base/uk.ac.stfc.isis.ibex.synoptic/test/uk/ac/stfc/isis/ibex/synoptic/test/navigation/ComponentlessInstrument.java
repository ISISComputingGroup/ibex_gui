package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class ComponentlessInstrument implements Instrument {

	@Override
	public String name() {
		return "Empty";
	}

	@Override
	public List<? extends Component> components() {
		return new ArrayList<>();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public InstrumentDescription getDescription() {
		// TODO Auto-generated method stub
		return null;
	}
}