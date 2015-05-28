package uk.ac.stfc.isis.ibex.synoptic.internal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentProperty;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;

public abstract class BaseComponent implements Component {

	private final ComponentDescription description;
	private Set<ComponentProperty> properties = new LinkedHashSet<>();
	private List<Component> components = new ArrayList<>();
	
	public BaseComponent(ComponentDescription description) {
		this.description = description;
	}

	public BaseComponent(BaseComponent other) {
		this.description = other.description;
		this.properties = other.properties;
		this.components = other.components;
	}
	
	@Override
	public String name() {
		return description.name();
	}

	@Override
	public ComponentType type() {
		return description.type();
	}

	@Override
	public Set<ComponentProperty> properties() {
		return new LinkedHashSet<>(properties);
	}

	@Override
	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}

	protected void addProperty(ComponentProperty property) {
		properties.add(property);
	}

	protected void addComponent(Component component) {
		components.add(component);
	}
	
	protected ComponentDescription description() {
		return description;
	}
}
