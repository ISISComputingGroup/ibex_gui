package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;

public class ParentComponent implements Component {

	private final String name;
	private final List<Component> components = new ArrayList<>();
	private final GroupedComponentTarget target;
	
	public ParentComponent(String name, Component...components) {
		this.name = name;
		this.components.addAll(Arrays.asList(components));
		target = new GroupedComponentTarget(name, this.components);
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ComponentType type() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<ComponentProperty> properties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<? extends Component> components() {
		return components;
	}

	@Override
	public Target target() {
		return target;
	}

	@Override
	public void setTarget(Target target) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component copy() {
		// TODO Auto-generated method stub
		return null;
	}
}
