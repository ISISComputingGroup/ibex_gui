package uk.ac.stfc.isis.ibex.synoptic.model;

import java.util.List;
import java.util.Set;

public interface Component {
	String name();

	ComponentType type();
	
	Set<ComponentProperty> properties();
	
	List<? extends Component> components();
	
	Target target();
	
	public void setTarget(Target target);
	
	public Component copy();
}
