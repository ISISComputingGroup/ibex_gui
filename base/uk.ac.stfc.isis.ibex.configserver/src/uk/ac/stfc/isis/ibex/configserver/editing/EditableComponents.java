// Class to provide the lists of selected and unselected Components for editing
package uk.ac.stfc.isis.ibex.configserver.editing;

import java.util.ArrayList;
import java.util.Collection;

import uk.ac.stfc.isis.ibex.configserver.configuration.Component;
import uk.ac.stfc.isis.ibex.model.ExclusiveSetPair;
import uk.ac.stfc.isis.ibex.model.ModelObject;

public class EditableComponents extends ModelObject {
	
	private final ExclusiveSetPair<Component> components;
	
	public EditableComponents(Collection<Component> selected, Collection<Component> available) {		
		components = new ExclusiveSetPair<Component>(available, selected);		
	}
	
	public Collection<Component> getUnselected() {
		return new ArrayList<>(components.unselected());
	}
	
	public Collection<Component> getSelected() {
		return new ArrayList<>(components.selected());
	}
	
	public synchronized void toggleSelection(Collection<Component> componentsToToggle) {
		Collection<Component> selectedBefore = getSelected();
		Collection<Component> unselectedBefore = getUnselected();
		
		for (Component component : componentsToToggle) {
			components.move(component);
		}
		
		firePropertyChange("selected", selectedBefore, getSelected());
		firePropertyChange("unselected", unselectedBefore, getUnselected());
	}
}
