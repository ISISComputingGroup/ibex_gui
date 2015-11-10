
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

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.configserver.editing.DefaultName;
import uk.ac.stfc.isis.ibex.instrument.Instrument;
import uk.ac.stfc.isis.ibex.instrument.pv.PVType;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.SynopticDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.target.DefaultTargetForComponent;

/**
 * Provides the model for the view of the synoptic. This is an observable
 * model, which various other classes subscribe to.
 * 
 */
public class SynopticViewModel {
	
	private SynopticModel EDITING = Synoptic.getInstance().edit();
	
	private SynopticDescription instrument;

	private List<ComponentDescription> selectedComponents;
	private PV selectedPV;
	private Property selectedProperty;

	private List<IInstrumentUpdateListener> instrumentUpdateListeners = new CopyOnWriteArrayList<>();
	private List<IComponentSelectionListener> componentSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPVSelectionListener> pvSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPropertySelectionListener> propertySelectionListeners = new CopyOnWriteArrayList<>();

	public final UpdatedValue<Boolean> canSaveCurrentSynoptic;

	public SynopticViewModel() {
		canSaveCurrentSynoptic = EDITING.saveSynoptic().canSave();
		loadCurrentInstrument();
	}
	
	public void loadInstrumentDescription(SynopticDescription description) {		
		instrument = description;
		instrument.processChildComponents();
		EDITING.setInstrumentFromDescription(description);
		
		setSelectedComponent(null);

		broadcastInstrumentUpdate(UpdateTypes.NEW_INSTRUMENT);
	}

	public SynopticDescription getInstrument() {
		return instrument;
	}

	public void addNewComponent() {
		ComponentDescription component = new ComponentDescription();
		component.setName("new component");
		component.setType(ComponentType.UNKNOWN);

		int position = 0;
		if (selectedComponents == null || selectedComponents.size() != 1) {
			instrument.addComponent(component);
		} else {
			ComponentDescription parent = selectedComponents.get(0).getParent();

			if (parent == null) {
				position = instrument.components().indexOf(selectedComponents) + 1;
				instrument.addComponent(component, position);
			} else {
				position = parent.components().indexOf(selectedComponents) + 1;
				parent.addComponent(component, position);
			}
		}

		broadcastInstrumentUpdate(UpdateTypes.NEW_COMPONENT);
		setSelectedComponent(Arrays.asList(component));

	}

    public void copySelected() {
        if (selectedComponents == null || selectedComponents.size() != 1) {
            return;
        }

        ComponentDescription selectedComponent = selectedComponents.get(0);
        
        ComponentDescription componentCopy = new ComponentDescription(selectedComponent);

        DefaultName namer = new DefaultName(selectedComponent.name(), " ", true);
        componentCopy.setName(namer.getUnique(instrument.getComponentNameList()));
        
        addComponentInCorrectLocation(componentCopy);

        // Set selected component here, so that it is auto-expanded.
        setSelectedComponent(Arrays.asList(componentCopy));
        broadcastInstrumentUpdate(UpdateTypes.COPY_COMPONENT);
        // Set selected component again here, so it is highlighted.
        setSelectedComponent(Arrays.asList(componentCopy));
    }

    private void addComponentInCorrectLocation(ComponentDescription component) {
    	ComponentDescription selectedComponent = selectedComponents.get(0);
    	
        ComponentDescription parent = selectedComponent.getParent();

        int position = 0;

        if (parent == null) {
            position = instrument.components().indexOf(selectedComponent) + 1;
            instrument.addComponent(component, position);
        } else {
            position = parent.components().indexOf(selectedComponent) + 1;
            parent.addComponent(component, position);
        }
    }

	public void removeSelected() {
		if (selectedComponents != null) {
			for (ComponentDescription selected : selectedComponents) {
				ComponentDescription parent = selected.getParent();
				if (parent == null) {
					instrument.removeComponent(selected);
				} else {
					parent.removeComponent(selected);
				}
			}
			setSelectedComponent(null);
			broadcastInstrumentUpdate(UpdateTypes.DELETE_COMPONENT);
		}
	}

	public int addNewPV() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			PV pv = new PV();
			pv.setDisplayName("New PV");
			pv.setAddress("NONE");
			RecordType rt = new RecordType();
			rt.setIO(IO.READ);
			pv.setRecordType(rt);
			pv.setPvType(PVType.LOCAL_PV);
	
			int index = 0;
			
			ComponentDescription selected = selectedComponents.get(0);
			
			if (selectedPV == null) {
				selected.addPV(pv);
			} else {
				index = selected.pvs().indexOf(selectedPV) + 1;
				selected.addPV(pv, index);
			}
	
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
			
			return index;
		}
		
		return 0;
	}

	public void promoteSelectedPV() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			selectedComponents.get(0).promotePV(selectedPV);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

	public void demoteSelectedPV() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			selectedComponents.get(0).demotePV(selectedPV);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

	public boolean canPromotePV() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			if (selectedPV == null) {
				return false;
			}
	
			int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
			return index > 0;
		} else {
			return false;
		}
	}

	public boolean canDemotePV() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			if (selectedPV == null) {
				return false;
			}
	
			int index = selectedComponents.get(0).pvs().indexOf(selectedPV);
			return index < selectedComponents.get(0).pvs().size() - 1;
		}else{
			return false;
		}
	}

	public void removeSelectedPV() {
		if (selectedComponents != null && selectedComponents.size() == 1 && selectedPV != null) {
			selectedComponents.get(0).removePV(selectedPV);
			setSelectedPV(null);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

	public void addTargetToSelectedComponent() {
		ComponentDescription component = getSelectedComponent();
        ComponentType compType = component.type();
        TargetDescription target = DefaultTargetForComponent.defaultTarget(compType);
		
        if (component != null && (component.target() == null || component.target().name() == "NONE")) {
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
	
	public ComponentDescription getSelectedComponent() {
		if (selectedComponents != null && selectedComponents.size() == 1) {
			return selectedComponents.get(0);
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
		switch (type){
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
		ComponentDescription component = getSelectedComponent();
		if (component != null && component.target() != null) {
			component.target().addProperty(property);
			broadcastInstrumentUpdate(UpdateTypes.NEW_PROPERTY);
		}
		setSelectedProperty(property);
	}

	public void removeSelectedProperty() {
		ComponentDescription component = getSelectedComponent();
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

		ComponentDescription component = getSelectedComponent();
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
		loadInstrumentDescription(EDITING.instrument().getDescription());
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
		instrument.setShowBeam(showBeam);
	}
	
	public Boolean getShowBeam() {
		return instrument.showBeam();
	}
}
