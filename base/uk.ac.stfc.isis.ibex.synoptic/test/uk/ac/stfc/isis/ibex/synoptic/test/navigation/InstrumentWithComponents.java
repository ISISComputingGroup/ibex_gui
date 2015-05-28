package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Instrument;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;

public class InstrumentWithComponents implements Instrument {

	private List<Component> components = new ArrayList<>();
	
	public InstrumentWithComponents(Component component, Component... components) {
		this.components.add(component);
		this.components.addAll(Arrays.asList(components));
	}

	@Override
	public String name() {
		return null;
	}

	@Override
	public List<? extends Component> components() {
		return components;
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
