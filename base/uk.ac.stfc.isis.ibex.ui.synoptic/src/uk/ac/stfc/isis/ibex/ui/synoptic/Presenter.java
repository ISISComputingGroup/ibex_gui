package uk.ac.stfc.isis.ibex.ui.synoptic;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.Component;
import uk.ac.stfc.isis.ibex.synoptic.model.Target;
import uk.ac.stfc.isis.ibex.synoptic.model.targets.GroupedComponentTarget;
import uk.ac.stfc.isis.ibex.synoptic.navigation.TargetNode;

public class Presenter extends ModelObject {
	
	private SynopticModel model;
	private NavigationPresenter navigator;
	private Map<String, Component> allComponents = new LinkedHashMap<>();
	private List<Component> components = new ArrayList<>();
	
	private List<String> trail;
	
	public Presenter() {
		model = Synoptic.getInstance().currentViewerModel();
		navigator = new NavigationPresenter(model.instrumentGraph().head());
		navigator.addPropertyChangeListener("currentTarget", new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent e) {
				displayTarget(navigator.currentTarget());
				firePropertyChange("currentTrail", trail, trail = generateCurrentTrail(navigator.currentNode()));
			}
		});
		
		setAllComponents(model.instrument().components());
		displayTarget(navigator.currentTarget());
		trail = generateCurrentTrail(navigator.currentNode());
	}
	
	public NavigationPresenter navigator() {
		return navigator;	
	}
	
	/*
	 * The hierarchy of components above the current component
	 */
	public List<String> currentTrail() {
		
		return trail;
	}

	private List<String> generateCurrentTrail(TargetNode targetNode) {
		List<String> trail = new ArrayList<>();
		
		TargetNode node = targetNode;
		if (node.item() instanceof GroupedComponentTarget) {
			trail.add(node.item().name());
		}
		
		while (node.up() != null) {
			node = node.up();
			if (node.item() instanceof GroupedComponentTarget) {
				trail.add(node.item().name());
			}
		}
		
		Collections.reverse(trail);
		return trail;
	}
	
	/*
	* A flattened hierarchy of all component names
	*/
	public List<String> allComponentNames() {
		return new ArrayList<>(allComponents.keySet());
	}
	
	public void navigateTo(final String componentName) {
		
	}
	

	public List<? extends Component> components() {
		return new ArrayList<>(components);
	}
	
	private void displayTarget(Target currentTarget) {
		if (currentTarget instanceof GroupedComponentTarget) {
			GroupedComponentTarget target = (GroupedComponentTarget) currentTarget;
			setComponents(target.components());
		}
	}
	
	private void setComponents(List<? extends Component> components) {
		this.components = new ArrayList<>(components);
		firePropertyChange("components", null, components());
	}
	
	private void setAllComponents(List<? extends Component> components) {
		List<Component> flattened = new ArrayList<>();
		addComponents(components, flattened);
		
		this.allComponents.clear();
		for (Component component : flattened) {
			this.allComponents.put(component.name(), component);
		}		
	}
	
	private void addComponents(List<? extends Component> from, List<Component> to) {
		for (Component component : from) {
			to.add(component);
			addComponents(component.components(), to);
		}
	}
}
