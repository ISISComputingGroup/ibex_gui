package uk.ac.stfc.isis.ibex.synoptic.model.targets;

import java.util.Arrays;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;

public class TargetBuilder {
		
	private final Component component;
	private final TargetDescription description;
	
	private Target target;
	
	public TargetBuilder(Component component) {
		this(component, null);
	}
	
	public TargetBuilder(Component component, TargetDescription description) {
		this.component = component;
		this.description = description;
		
		target = getTarget(component);
	}
	
	public Target target() {
		return target;
	}
	
	private Target getTarget(Component component) {		
		Target target = componentTarget() != null ? componentTarget() : targetFromType(description);
		return isGroup() ? targetForGroup(target) : target;
	}
	
	private Target componentTarget() {
		return component.type().target(); 
	}
	
	private Target targetForGroup(Target target) {
		// A group's first target is the grouped view; the second, the actual target.
		Component copy = component.copy();
		copy.setTarget(target);
		return new GroupedComponentTarget(component.name(), Arrays.asList(copy));
	}

	private Target targetFromType(TargetDescription description) {
		if (description == null) {
			return null;
		}
		
		switch (description.type()) {
			case OPI:
				return opiTarget(description);
			default:
				return null;
		}
	}

	private Target opiTarget(TargetDescription description) {		
		String name = component.name();
		OpiTarget opiTarget = new OpiTarget(name, description.name());
		for (Property property : description.properties()) {
			opiTarget.addProperty(property.key(), property.value());
		}
		
		return opiTarget;
	}
	
	
	private boolean isGroup() {
		return !component.components().isEmpty();
	}
}
