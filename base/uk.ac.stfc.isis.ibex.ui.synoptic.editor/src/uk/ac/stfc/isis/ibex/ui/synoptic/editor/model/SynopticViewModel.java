
/*
 * This file is part of the ISIS IBEX application. Copyright (C) 2012-2016
 * Science & Technology Facilities Council. All rights reserved.
 *
 * This program is distributed in the hope that it will be useful. This program
 * and the accompanying materials are made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution. EXCEPT AS
 * EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM AND
 * ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES
 * OR CONDITIONS OF ANY KIND. See the Eclipse Public License v1.0 for more
 * details.
 *
 * You should have received a copy of the Eclipse Public License v1.0 along with
 * this program; if not, you can obtain a copy from
 * https://www.eclipse.org/org/documents/epl-v10.php or
 * http://opensource.org/licenses/eclipse-1.0.php
 */

/*
 * Copyright (C) 2013-2014 Research Councils UK (STFC)
 *
 * This file is part of the Instrument Control Project at ISIS.
 *
 * This code and information are provided "as is" without warranty of any 
 * kind, either expressed or implied, including but not limited to the
 * implied warranties of merchantability and/or fitness for a particular 
 * purpose.
 */
package uk.ac.stfc.isis.ibex.ui.synoptic.editor.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticParentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.dialogs.SuggestedTargetsDialog;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.DefaultTargetForComponent;

/**
 * Provides the model for the view of the synoptic. This is an observable model,
 * which various other classes subscribe to.
 */
public class SynopticViewModel extends ModelObject {
	private SynopticModel editing = Synoptic.getInstance().edit();
	private SynopticDescription synoptic;
	private List<ComponentDescription> selectedComponents;
	private Property selectedProperty;
	private List<IInstrumentUpdateListener> instrumentUpdateListeners = new CopyOnWriteArrayList<>();

    /**
     * The constructor.
     * 
     * @param description
     *            The description of the synoptic to edit.
     */
    public SynopticViewModel(SynopticDescription description) {
        loadSynopticDescription(description);
	}
	
    /**
     * Loads a synoptic description for editing etc.
     * 
     * @param description the description
     */
    public void loadSynopticDescription(SynopticDescription description) {
        synoptic = description;
        synoptic.processChildComponents();
		editing.setSynopticFromDescription(description);
		
		setSelectedComponent(null);

		broadcastInstrumentUpdate(UpdateTypes.NEW_INSTRUMENT);
	}

    /**
     * Gets the current synoptic description.
     * 
     * @return the synoptic
     */
	public SynopticDescription getSynoptic() {
		return synoptic;
	}

    /**
     * Gets the parent synoptic of a particular component.
     * 
     * @param component the component
     * @return the parent
     */
	public SynopticParentDescription getParent(ComponentDescription component) {
		SynopticParentDescription parent = component.getParent();
		if (parent == null) {
			return synoptic;
		}
		return parent;
	}

    /**
     * Get a name that is unique for an object.
     * 
     * @param rootName
     *            The ideal name for the object
     * @param existingNames
     *            The existing names
     * @return A unique name
     */
    public String getUniqueName(String rootName, List<String> existingNames) {
        DefaultName namer = new DefaultName(rootName, " ", true);
        return namer.getUnique(existingNames);
    }

    /**
     * Add a new blank component to the synoptic.
     */
	public void addNewComponent() {
		ComponentDescription component = new ComponentDescription();

        component.setName(getUniqueName("New Component", synoptic.getComponentNameListWithChildren()));
		component.setType(ComponentType.UNKNOWN);

		int position = 0;
		if (selectedComponents == null) {
			synoptic.addComponent(component);
		} else {
			ComponentDescription lastComponent = selectedComponents.get(selectedComponents.size() - 1);
			SynopticParentDescription parent = getParent(lastComponent);
			position = parent.components().indexOf(lastComponent) + 1;
			parent.addComponent(component, position);
            component.setParent(parent);
		}

        refreshTreeView();
		setSelectedComponent(Arrays.asList(component));

	}

    /**
     * Allows the selected component(s) to be copied.
     */
    public void copySelectedComponent() {
        if (selectedComponents == null || selectedComponents.isEmpty()) {
            return;
        }

    	List<ComponentDescription> componentCopies = new ArrayList<>();

        List<String> allComponentNames = synoptic.getComponentNameListWithChildren();
        
        for (ComponentDescription selectedComponent : selectedComponents) {
	        ComponentDescription componentCopy = new ComponentDescription(selectedComponent);

            String uniqueName = getUniqueName(selectedComponent.name(), allComponentNames);
            allComponentNames.add(uniqueName);

            componentCopy.setName(uniqueName);
	        
            setUniqueChildNames(componentCopy, allComponentNames);

	        componentCopies.add(componentCopy);
        }
        
        addComponentsInCorrectLocation(componentCopies);
        
        // Set selected component here, so that it is auto-expanded.
        setSelectedComponent(componentCopies);
        broadcastInstrumentUpdate(UpdateTypes.COPY_COMPONENT);
        // Set selected component again here, so it is highlighted.
        setSelectedComponent(componentCopies);
    }

    private void setUniqueChildNames(ComponentDescription component, List<String> allComponentNames) {
        for (ComponentDescription cd : component.components()) {
            String uniqueName = getUniqueName(cd.name(), allComponentNames);
            allComponentNames.add(uniqueName);
            
            cd.setName(uniqueName);
            setUniqueChildNames(cd, allComponentNames);
        }
    }

    private void addComponentsInCorrectLocation(List<ComponentDescription> componentCopies) {
    	ComponentDescription lastSelectedComponent = selectedComponents.get(selectedComponents.size() - 1);
    	
        SynopticParentDescription parent = getParent(lastSelectedComponent);

        int position = 0;
    	int idx = 1;
        
        for (ComponentDescription componentCopy : componentCopies) {
	        position = parent.components().indexOf(lastSelectedComponent) + idx;
	        parent.addComponent(componentCopy, position);
	        idx += 1;
    	}
    }

    private String constructDeleteMessage() {
		String message = "Are you sure you want to delete the component";
		if (selectedComponents.size() == 1) {
            message += " " + getSingleSelectedComp().name() + "?";
		} else {
			int idx = 0;
            message += "s ";
			for (ComponentDescription selected : selectedComponents) {
				message += selected.name();
				if (idx == selectedComponents.size() - 2) {
					message += " and ";
				} else if (idx != selectedComponents.size() - 1) {
					message += ", ";
				}
				idx++;
			}
			message += "?";
		}
		return message;
    }
    
    /**
     * Removes the selected component(s) from the synoptic.
     */
	public void removeSelectedComponent() {
		if (selectedComponents != null) {
			Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
			if (MessageDialog.openConfirm(shell, "Confirm Delete", constructDeleteMessage())) {
				for (ComponentDescription selected : selectedComponents) {
					SynopticParentDescription parent = getParent(selected);
					parent.removeComponent(selected);
				}
				setSelectedComponent(null);
                refreshTreeView();
			}
		}
	}

    /**
     * Add a target (e.g. an OPI) to the component.
     * 
     * @param isFinalEdit has focus moved onto something else (e.g another
     *            component)
     */
    public void addTargetToSelectedComponent(boolean isFinalEdit) {
        ComponentDescription component = getSingleSelectedComp();
        ComponentType compType = component.type();

        Collection<TargetDescription> potentialTargets = DefaultTargetForComponent.defaultTarget(compType);
        TargetDescription target = new TargetDescription("NONE", TargetType.OPI);

        if (potentialTargets.size() == 1) {
            target = potentialTargets.iterator().next();
        } else if (potentialTargets.size() > 1 && isFinalEdit && !component.target().getUserSelected()) {
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
            SuggestedTargetsDialog dialog = new SuggestedTargetsDialog(shell, potentialTargets);
            dialog.open();
            target = dialog.selectedTarget();
        }
		
        if (component != null && (component.target() == null || !component.target().getUserSelected())) {
            target.setUserSelected(isFinalEdit);
            component.setTarget(target);
            target.addProperties(getPropertyKeys(target.name()));
			broadcastInstrumentUpdate(UpdateTypes.ADD_TARGET);
		}

	}

    /**
     * Sets which component(s) is/are selected.
     * 
     * @param selected
     *            the selected component(s)
     */
    public void setSelectedComponent(List<ComponentDescription> selected) {
        firePropertyChange("compSelection", selectedComponents, selectedComponents = selected);
        setSelectedProperty(null);
        refreshTreeView();
    }
	
    /**
     * Gets the selected component if there is only one, otherwise returns null.
     * 
     * @return the selected component or null
     */
    public ComponentDescription getSingleSelectedComp() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			return selectedComponents.get(0);
		} else {
			return null;
		}
	}
	
    /**
     * Get all the selected components.
     * 
     * @return the selected components
     */
	public List<ComponentDescription> getSelectedComponents() {
		if (selectedComponents != null) {
			return selectedComponents;
		} else {
			return null;
		}
	}

    /**
     * Set the currently selected property.
     * 
     * @param selected the property
     */
	public void setSelectedProperty(Property selected) {
        firePropertyChange("propSelection", selectedProperty, selectedProperty = selected);
	}

    /**
     * Gets the currently selected property. Can be null.
     * 
     * @return the property
     */
	public Property getSelectedProperty() {
		return selectedProperty;
	}

    /**
     * Update or add the selected property.
     * 
     * Will try to update an existing property but if it does not exist then
     * will add it to the list of properties.
     *
     * @param newProperty the new property
     */
	public void updateOrAddSelectedProperty(Property newProperty) {
		if (newProperty == null) {
			return;
		}

        ComponentDescription component = getSingleSelectedComp();
		if (component != null) {
			TargetDescription target = component.target();
			if (target != null) {
                target.replaceOrAddProperty(newProperty);
				broadcastInstrumentUpdate(UpdateTypes.EDIT_PROPERTY);
				setSelectedProperty(newProperty);
			}
		}
	}

    /**
     * Add a listener for changes to the instrument.
     * 
     * @param listener the listener to add
     */
	public void addInstrumentUpdateListener(IInstrumentUpdateListener listener) {
		if (listener == null) {
			return;
		}

		instrumentUpdateListeners.add(listener);
	}

    /**
     * Let the listeners know that the instrument has updated.
     * 
     * @param updateType the type of update
     */
	public void broadcastInstrumentUpdate(UpdateTypes updateType) {
		for (IInstrumentUpdateListener listener : instrumentUpdateListeners) {
			listener.instrumentUpdated(updateType);
		}
	}
	
    /**
     * Whether to show the beam in the synoptic.
     * 
     * @param showBeam true means show it
     */
	public void setShowBeam(Boolean showBeam) {
		synoptic.setShowBeam(showBeam);
	}
	
    /**
     * Whether the synoptic is set to show the beam or not.
     * 
     * @return true means show it
     */
	public Boolean getShowBeam() {
		return synoptic.showBeam();
	}
	
    /**
     * Whether the selected components have the same parent.
     * 
     * @return true for the same
     */
	public Boolean selectedHaveSameParent() {
		SynopticParentDescription parent = selectedComponents.get(0).getParent();
		for (ComponentDescription selected : selectedComponents) {
			if (selected.getParent() != parent) {
				return false;
			}
		}
		return true;
	}

    /**
     * Gets the OPI for the specified target.
     * 
     * @param targetName the name of the OPI.
     * @return the OPI description
     */
    public OpiDescription getOpi(String targetName) {
        String name = Opi.getDefault().descriptionsProvider().guessOpiName(targetName);
        OpiDescription opi = Opi.getDefault().descriptionsProvider().getDescription(name);
        return opi;
	}
    
    /**
     * Gets the OPI property keys for a given target.
     *
     * @param targetName the target name
     * @return the property keys
     */
    public List<String> getPropertyKeys(String targetName) {
        return getOpi(targetName).getKeys();
    }

    /**
     * Gets the OPI property keys for the selected target.
     *
     * @return the property keys
     */
    public List<String> getSelectedPropertyKeys() {
        return getPropertyKeys(getSingleSelectedComp().target().name());
    }

    /**
     * Refreshes the tree view of the instrument.
     */
    public void refreshTreeView() {
        firePropertyChange("refreshTree", 0, 1);
    }
}
