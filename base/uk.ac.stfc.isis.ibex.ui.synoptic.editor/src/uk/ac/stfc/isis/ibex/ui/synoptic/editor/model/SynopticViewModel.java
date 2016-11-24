
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.devicescreens.components.ComponentType;
import uk.ac.stfc.isis.ibex.opis.Opi;
import uk.ac.stfc.isis.ibex.opis.desc.OpiDescription;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;
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
public class SynopticViewModel {
	private SynopticModel editing = Synoptic.getInstance().edit();
	private SynopticDescription synoptic;
	private List<ComponentDescription> selectedComponents;
	private PV selectedPV;
	private Property selectedProperty;
	private List<IInstrumentUpdateListener> instrumentUpdateListeners = new CopyOnWriteArrayList<>();
	private List<IComponentSelectionListener> componentSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPVSelectionListener> pvSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPropertySelectionListener> propertySelectionListeners = new CopyOnWriteArrayList<>();

    /**
     * The constructor.
     */
	public SynopticViewModel() {
		loadCurrentInstrument();
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
     * Add a new blank component to the synoptic.
     */
	public void addNewComponent() {
		ComponentDescription component = new ComponentDescription();

        DefaultName namer = new DefaultName("New Component", " ", true);
        component.setName(namer.getUnique(synoptic.getComponentNameListWithChildren()));
        component.setType(ComponentType.UNKNOWN, false);

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

		broadcastInstrumentUpdate(UpdateTypes.NEW_COMPONENT);
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

	        DefaultName namer = new DefaultName(selectedComponent.name(), " ", true);
            String uniqueName = namer.getUnique(allComponentNames);
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
            DefaultName namer = new DefaultName(cd.name(), " ", true);
            String uniqueName = namer.getUnique(allComponentNames);
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
			message += " " + getFirstSelectedComponent().name() + "?";
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
				broadcastInstrumentUpdate(UpdateTypes.DELETE_COMPONENT);
			}
		}
	}

    /**
     * Adds a PV writer/reader to the component.
     * 
     * @return the index
     */
	public int addNewPV() {
		PV pv = new PV();
		pv.setDisplayName("New PV");
		pv.setAddress("NONE");
		RecordType rt = new RecordType();
		rt.setIO(IO.READ);
		pv.setRecordType(rt);

		int index = 0;
		
		ComponentDescription selected = getFirstSelectedComponent();
		
		if (selectedPV == null) {
			selected.addPV(pv);
		} else {
			index = selected.pvs().indexOf(selectedPV) + 1;
			selected.addPV(pv, index);
		}

		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		
		return index;
	}

    /**
     * Moves a PV up the list.
     */
	public void promoteSelectedPV() {
		getFirstSelectedComponent().promotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

    /**
     * Moves a PV down the list.
     */
	public void demoteSelectedPV() {
		getFirstSelectedComponent().demotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

    /**
     * Determines whether the PV be moved up (enables or disable the associated
     * control).
     * 
     * @return true means can promote
     */
	public boolean canPromotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
		return index > 0;
	}

    /**
     * Determines whether the PV be moved down (enables or disable the
     * associated control).
     * 
     * @return true means can promote
     */
	public boolean canDemotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
		return index < selectedComponents.get(0).pvs().size() - 1;
	}

    /**
     * Remove the selected PV from the component.
     */
	public void removeSelectedPV() {
		if (selectedPV != null) {
			selectedComponents.get(0).removePV(selectedPV);
			setSelectedPV(null);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

    /**
     * Add a target (e.g. an OPI) to the component.
     * 
     * @param isFinalEdit has focus moved onto something else (e.g another
     *            component)
     */
    public void addTargetToSelectedComponent(boolean isFinalEdit) {
		ComponentDescription component = getFirstSelectedComponent();
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
     * @param selected the selected component(s)
     */
	public void setSelectedComponent(List<ComponentDescription> selected) {
		broadcastComponentSelectionChanged(selectedComponents,
				selectedComponents = selected);
		setSelectedPV(null);
		setSelectedProperty(null);
	}
	
    /**
     * Gets the first selected component as many could be selected.
     * 
     * @return the first component selected
     */
	public ComponentDescription getFirstSelectedComponent() {
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
     * Change the settings for the selected PV.
     * 
     * @param name the display name
     * @param address the underlying PV
     * @param mode whether it is read or write
     */
    public void updateSelectedPV(String name, String address, IO mode) {
		if (selectedPV == null) {
			return;
		}

		selectedPV.setDisplayName(name);
		RecordType recordType = new RecordType();
		recordType.setIO(mode);
		selectedPV.setRecordType(recordType);

		String addressToUse = address;
		
		selectedPV.setAddress(addressToUse);
		
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

    /**
     * Set the selected PV.
     * 
     * @param selected the PV
     */
	public void setSelectedPV(PV selected) {
		broadcastPVSelectionChanged(selectedPV, selectedPV = selected);
	}

    /**
     * Gets the currently selected PV. Can be null.
     * 
     * @return the PV
     */
	public PV getSelectedPV() {
		return selectedPV;
	}

    /**
     * Set the currently selected property.
     * 
     * @param selected the property
     */
	public void setSelectedProperty(Property selected) {
		broadcastPropertySelectionChanged(selectedProperty,
				selectedProperty = selected);
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
     * Add a listener for a change in the selected property.
     * 
     * @param listener the listener to add
     */
	public void addPropertySelectionListener(IPropertySelectionListener listener) {
		if (listener == null) {
			return;
		}

		propertySelectionListeners.add(listener);
	}

    /**
     * Remove a listener to the change of selected property.
     * 
     * @param listener the listener to remove
     */
	public void removePropertySelectionListener(
			IPropertySelectionListener listener) {
		propertySelectionListeners.remove(listener);
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

		ComponentDescription component = getFirstSelectedComponent();
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
     * Add a listener for changes in which component is selected.
     * 
     * @param listener the listener to add
     */
	public void addComponentSelectionListener(
			IComponentSelectionListener listener) {
		if (listener == null) {
			return;
		}

		componentSelectionListeners.add(listener);
	}

	private void broadcastComponentSelectionChanged(
			List<ComponentDescription> oldSelected, List<ComponentDescription> newSelected) {
		for (IComponentSelectionListener listener : componentSelectionListeners) {
			listener.selectionChanged(oldSelected, newSelected);
		}
	}
	
    /**
     * Add a listener for changes in which PV is selected.
     * 
     * @param listener the listener to add
     */
	public void addPVSelectionListener(IPVSelectionListener listener) {
		if (listener == null) {
			return;
		}

		pvSelectionListeners.add(listener);
	}
	
	private void loadCurrentInstrument() {
		loadSynopticDescription(editing.instrument().getDescription());
	}

	private void broadcastPVSelectionChanged(PV oldSelected, PV newSelected) {
		for (IPVSelectionListener listener : pvSelectionListeners) {
			listener.selectionChanged(oldSelected, newSelected);
		}
	}

	private void broadcastPropertySelectionChanged(Property oldProperty,
			Property newProperty) {
		for (IPropertySelectionListener listener : propertySelectionListeners) {
			listener.selectionChanged(oldProperty, newProperty);
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
     * Checks for duplicate names in the components.
     * 
     * @return true means duplicate(s)
     */
    public boolean getHasDuplicatedName() {
        List<String> comps = getSynoptic().getComponentNameListWithChildren();
        return listHasDuplicates(comps);
    }

    private boolean listHasDuplicates(List<String> list) {
        Set<String> set = new HashSet<String>(list);
        return (set.size() < list.size());
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
        return getPropertyKeys(getFirstSelectedComponent().target().name());
    }
}
