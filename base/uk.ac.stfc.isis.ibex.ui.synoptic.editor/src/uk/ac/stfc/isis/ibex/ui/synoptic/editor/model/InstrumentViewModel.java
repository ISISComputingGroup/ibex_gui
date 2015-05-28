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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import uk.ac.stfc.isis.ibex.model.UpdatedValue;
import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticModel;
import uk.ac.stfc.isis.ibex.synoptic.model.ComponentType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.ComponentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.IO;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.InstrumentDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.PV;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.RecordType;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetDescription;
import uk.ac.stfc.isis.ibex.synoptic.model.desc.TargetType;

public class InstrumentViewModel {
	
	private SynopticModel EDITING = Synoptic.getInstance().edit();
	
	private InstrumentDescription instrument;

	private ComponentDescription selectedComponent;
	private PV selectedPV;
	private Property selectedProperty;

	private List<IInstrumentUpdateListener> instrumentUpdateListeners = new CopyOnWriteArrayList<>();
	private List<IComponentSelectionListener> componentSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPVSelectionListener> pvSelectionListeners = new CopyOnWriteArrayList<>();
	private List<IPropertySelectionListener> propertySelectionListeners = new CopyOnWriteArrayList<>();

	public final UpdatedValue<Boolean> canSaveCurrentSynoptic;

	public InstrumentViewModel() {
		canSaveCurrentSynoptic = EDITING.saveSynoptic().canSave();
		loadCurrentInstrument();
	}
	
	public void loadInstrumentDescription(InstrumentDescription description) {		
		instrument = description;
		instrument.processChildComponents();
		EDITING.setInstrumentFromDescription(description);
		
		setSelectedComponent(null);

		broadcastInstrumentUpdate(UpdateTypes.NEW_INSTRUMENT);
	}

	public InstrumentDescription getInstrument() {
		return instrument;
	}

	public void addNewComponent() {
		ComponentDescription component = new ComponentDescription();
		component.setName("new component");
		component.setType(ComponentType.UNKNOWN);

		int position = 0;
		if (selectedComponent == null) {
			instrument.addComponent(component);
		} else {
			ComponentDescription parent = selectedComponent.getParent();

			if (parent == null) {
				position = instrument.components().indexOf(selectedComponent) + 1;
				instrument.addComponent(component, position);
			} else {
				position = parent.components().indexOf(selectedComponent) + 1;
				parent.addComponent(component, position);
			}
		}

		broadcastInstrumentUpdate(UpdateTypes.NEW_COMPONENT);
		setSelectedComponent(component);
	}

	public void removeSelected() {
		if (selectedComponent != null) {
			ComponentDescription parent = selectedComponent.getParent();
			if (parent == null) {
				instrument.removeComponent(selectedComponent);
			} else {
				parent.removeComponent(selectedComponent);
			}

			broadcastInstrumentUpdate(UpdateTypes.DELETE_COMPONENT);
		}
	}

	public int addNewPV() {
		PV pv = new PV();
		pv.setDisplayName("New PV");
		pv.setAddress("NONE");
		RecordType rt = new RecordType();
		rt.setIO(IO.READ);
		pv.setRecordType(rt);

		int index = 0;
		
		if (selectedPV == null) {
			selectedComponent.addPV(pv);
		} else {
			index = selectedComponent.pvs().indexOf(selectedPV) + 1;
			selectedComponent.addPV(pv, index);
		}

		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		
		return index;
	}

	public void promoteSelectedPV() {
		selectedComponent.promotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

	public void demoteSelectedPV() {
		selectedComponent.demotePV(selectedPV);
		broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
	}

	public boolean canPromotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponent.pvs().indexOf(selectedPV);
		return index > 0;
	}

	public boolean canDemotePV() {
		if (selectedPV == null) {
			return false;
		}

		int index = selectedComponent.pvs().indexOf(selectedPV);
		return index < selectedComponent.pvs().size() - 1;
	}

	public void removeSelectedPV() {
		if (selectedComponent != null && selectedPV != null) {
			selectedComponent.removePV(selectedPV);
			setSelectedPV(null);
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PV);
		}
	}

	public void addTargetToSelectedComponent() {
		ComponentDescription component = getSelectedComponent();
		if (component != null && component.target() == null) {
			TargetDescription target = new TargetDescription();
			target.setName("NONE");
			target.setType(TargetType.COMPONENT);
			component.setTarget(target);

			broadcastInstrumentUpdate(UpdateTypes.ADD_TARGET);
		}
	}

	public void setSelectedComponent(ComponentDescription selected) {
		broadcastComponentSelectionChanged(selectedComponent,
				selectedComponent = selected);
		setSelectedPV(null);
		setSelectedProperty(null);
	}
	
	public ComponentDescription getSelectedComponent() {
		return selectedComponent;
	}

	public void updateSelectedPV(String name, String address, IO mode) {
		if (selectedPV == null) {
			return;
		}

		selectedPV.setDisplayName(name);
		selectedPV.setAddress(address);
		RecordType recordType = new RecordType();
		recordType.setIO(mode);
		selectedPV.setRecordType(recordType);

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
			broadcastInstrumentUpdate(UpdateTypes.EDIT_PROPERTY);
		}
	}

	public void removeSelectedProperty() {
		ComponentDescription component = getSelectedComponent();
		if (component != null && component.target() != null) {
			if (component.target().removeProperty(getSelectedProperty())) {
				setSelectedProperty(null);
				broadcastInstrumentUpdate(UpdateTypes.EDIT_PROPERTY);
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
			ComponentDescription oldSelected, ComponentDescription newSelected) {
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
}
