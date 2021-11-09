package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus>, PropertyChangeListener {
	
	
	protected HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction;
	
	public DynamicScriptingState(HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		this.dynamicScriptIdsToAction = dynamicScriptIdsToAction;
	}
	
	public Boolean isScriptDynamic(Integer scriptId) {
		return dynamicScriptIdsToAction.containsKey(scriptId);
	}
	
	public void changeState(DynamicScriptingStatus newStatus) {
		firePropertyChange(DynamicScriptingProperties.STATE_CHANGE_PROPERTY, getStatus(), newStatus);
	}
	
	public Optional<ScriptGeneratorAction> getCurrentlyExecutingAction() {
		return Optional.empty();
	}

	public Optional<ScriptGeneratorAction> getNextExecutingAction() {
		return Optional.empty();
	}
	
	public void play() { /* Default function does nothing */ };
	
	public void stop() { /* Default function does nothing */ };
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) { /* Default function does nothing */ };
	
	public abstract DynamicScriptingStatus getStatus();
	
}
