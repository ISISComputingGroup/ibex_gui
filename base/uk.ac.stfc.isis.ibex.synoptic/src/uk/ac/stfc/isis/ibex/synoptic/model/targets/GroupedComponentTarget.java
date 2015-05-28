package uk.ac.stfc.isis.ibex.synoptic.model.targets;

import java.util.ArrayList;
import java.util.List;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;

public class GroupedComponentTarget extends Target {

	private final List<Component> components;
	
	public GroupedComponentTarget(String name, List<? extends Component> components) {
		super(name);
		this.components = new ArrayList<>(components);
	}
	
	public List<Component> components() {
		return components;
	}	
}
