package uk.ac.stfc.isis.ibex.synoptic.test.navigation;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;

public class ChildlessComponent implements Component {

	private String name;
	private GroupedComponentTarget target;
	
	public ChildlessComponent(String name) {
		this.name = name;
		target = new GroupedComponentTarget(name, new ArrayList<Component>());
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
		return new ArrayList<Component>();
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
