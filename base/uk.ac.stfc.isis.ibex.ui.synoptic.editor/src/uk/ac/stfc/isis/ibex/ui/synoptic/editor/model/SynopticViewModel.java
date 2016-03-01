
/*
* This file is part of the ISIS IBEX application.
* Copyright (C) 2012-2015 Science & Technology Facilities Council.
* All rights reserved.
*
* This program is distributed in the hope that it will be useful.
* This program and the accompanying materials are made available under the
* terms of the Eclipse Public License v1.0 which accompanies this distribution.
* EXCEPT AS EXPRESSLY SET FORTH IN THE ECLIPSE PUBLIC LICENSE V1.0, THE PROGRAM 
* AND ACCOMPANYING MATERIALS ARE PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES 
* OR CONDITIONS OF ANY KIND.  See the Eclipse Public License v1.0 for more details.
*
* You should have received a copy of the Eclipse Public License v1.0
* along with this program; if not, you can obtain a copy from
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
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
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
 * Provides the model for the view of the synoptic. This is an observable
 * model, which various other classes subscribe to.
 * 
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

	public SynopticViewModel() {
		loadCurrentInstrument();
	}
	
    public void loadSynopticDescription(SynopticDescription description) {
        synoptic = description;
        synoptic.processChildComponents();
		editing.setSynopticFromDescription(description);
		
		setSelectedComponent(null);

		broadcastInstrumentUpdate(UpdateTypes.NEW_INSTRUMENT);
	}

	public SynopticDescription getSynoptic() {
		return synoptic;
	}

	public SynopticParentDescription getParent(ComponentDescription component) {
		SynopticParentDescription parent = component.getParent();
		if (parent == null) {
			return synoptic;
		}
		return parent;
	}
	 
	public void addNewComponent() {
		ComponentDescription component = new ComponentDescription();

        DefaultName namer = new DefaultName("New Component", " ", true);
        component.setName(namer.getUnique(synoptic.getComponentNameListWithChildren()));
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

		broadcastInstrumentUpdate(UpdateTypes.NEW_COMPONENT);
		setSelectedComponent(Arrays.asList(component));

	}

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

	public int addNewPV() {
		PV pv = new PV();
		pv.setDisplayName("New PV");
		pv.setAddress("NONE");
		RecordType rt = new RecordType();
		rt.setIO(IO.READ);
		pv.setRecordType(rt);
		pv.setPvType(PVType.LOCAL_PV);

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

	public void promoteSelectedPV() {
		getFirstSelectedComponent().promotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

	public void demoteSelectedPV() {
		getFirstSelectedComponent().demotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

	public boolean canPromotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
		return index > 0;
	}

	public boolean canDemotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
		return index < selectedComponents.get(0).pvs().size() - 1;
	}

	public void removeSelectedPV() {
		if (selectedPV != null) {
			selectedComponents.get(0).removePV(selectedPV);
			setSelectedPV(null);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

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
			broadcastInstrumentUpdate(UpdateTypes.ADD_TARGET);
		}

	}

	public void setSelectedComponent(List<ComponentDescription> selected) {
		broadcastComponentSelectionChanged(selectedComponents,
				selectedComponents = selected);
		setSelectedPV(null);
		setSelectedProperty(null);
	}
	
	public ComponentDescription getFirstSelectedComponent() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			return selectedComponents.get(0);
		} else {
			return null;
		}
	}
	
	public List<ComponentDescription> getSelectedComponents() {
		if (selectedComponents != null) {
			return selectedComponents;
		} else {
			return null;
		}
	}

	public void updateSelectedPV(String name, String address, IO mode, PVType type) {
		if (selectedPV == null) {
			return;
		}

		selectedPV.setDisplayName(name);
		RecordType recordType = new RecordType();
		recordType.setIO(mode);
		selectedPV.setRecordType(recordType);
		selectedPV.setPvType(type);

		String addressToUse = address;
		switch (type) {
			case LOCAL_PV:
				String pvprefix = Instrument.getInstance().currentInstrument().pvPrefix();
				addressToUse = addressToUse.replace(pvprefix, "");
				break;
			default:
				//Leave the address as that entered - could be remote or a block
				break;
		}
		
		selectedPV.setAddress(addressToUse);
		
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

	public void setSelectedPV(PV selected) {
		broadcastPVSelectionChanged(selectedPV, selectedPV = selected);
	}

	public PV getSelectedPV() {
		return selectedPV;
	}

	public void setSelectedProperty(Property selected) {
		broadcastPropertySelectionChanged(selectedProperty,
				selectedProperty = selected);
	}

	public Property getSelectedProperty() {
		return selectedProperty;
	}

	public void addNewProperty() {
		Property property = new Property("?", "?");
		ComponentDescription component = getFirstSelectedComponent();
		if (component != null && component.target() != null) {
			component.target().addProperty(property);
			broadcastInstrumentUpdate(UpdateTypes.NEW_PROPERTY);
		}
		setSelectedProperty(property);
	}

	public void removeSelectedProperty() {
		ComponentDescription component = getFirstSelectedComponent();
		if (component != null && component.target() != null) {
			if (component.target().removeProperty(getSelectedProperty())) {
				setSelectedProperty(null);
				broadcastInstrumentUpdate(UpdateTypes.DELETE_PROPERTY);
			}
		}
	}

	public void addPropertySelectionListener(IPropertySelectionListener listener) {
		if (listener == null) {
			return;
		}

		propertySelectionListeners.add(listener);
	}

	public void removePropertySelectionListener(
			IPropertySelectionListener listener) {
		propertySelectionListeners.remove(listener);
	}

	public void updateSelectedProperty(Property newProperty) {
		if (newProperty == null) {
			return;
		}

		Property current = getSelectedProperty();
		if (newProperty == current) {
			return;
		}

		ComponentDescription component = getFirstSelectedComponent();
		if (component != null) {
			TargetDescription target = component.target();
			if (target != null) {
				target.replaceProperty(current, newProperty);
				broadcastInstrumentUpdate(UpdateTypes.EDIT_PROPERTY);
				setSelectedProperty(newProperty);
			}
		}
	}

	public void addInstrumentUpdateListener(IInstrumentUpdateListener listener) {
		if (listener == null) {
			return;
		}

		instrumentUpdateListeners.add(listener);
	}

	public void removeInstrumentUpdateListener(
			IInstrumentUpdateListener listener) {
		instrumentUpdateListeners.remove(listener);
	}

	public void broadcastInstrumentUpdate(UpdateTypes updateType) {
		for (IInstrumentUpdateListener listener : instrumentUpdateListeners) {
			listener.instrumentUpdated(updateType);
		}
	}

	public void addComponentSelectionListener(
			IComponentSelectionListener listener) {
		if (listener == null) {
			return;
		}

		componentSelectionListeners.add(listener);
	}
	
	public void removeComponentSelectionListener(
			IComponentSelectionListener listener) {
		componentSelectionListeners.remove(listener);
	}

	private void broadcastComponentSelectionChanged(
			List<ComponentDescription> oldSelected, List<ComponentDescription> newSelected) {
		for (IComponentSelectionListener listener : componentSelectionListeners) {
			listener.selectionChanged(oldSelected, newSelected);
		}
	}
	
	public void addPVSelectionListener(IPVSelectionListener listener) {
		if (listener == null) {
			return;
		}

		pvSelectionListeners.add(listener);
	}

	public void removePVSelectionListener(IPVSelectionListener listener) {
		pvSelectionListeners.remove(listener);
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
	
	public void setShowBeam(Boolean showBeam) {
		synoptic.setShowBeam(showBeam);
	}
	
	public Boolean getShowBeam() {
		return synoptic.showBeam();
	}
	
	public Boolean selectedHaveSameParent() {
		SynopticParentDescription parent = selectedComponents.get(0).getParent();
		for (ComponentDescription selected : selectedComponents) {
			if (selected.getParent() != parent) {
				return false;
			}
		}
		return true;
	}

    public boolean getHasDuplicatedName() {
        List<String> comps = getSynoptic().getComponentNameListWithChildren();
        return listHasDuplicates(comps);
    }

    private boolean listHasDuplicates(List<String> list) {
        Set<String> set = new HashSet<String>(list);
        return (set.size() < list.size());
    }
}
