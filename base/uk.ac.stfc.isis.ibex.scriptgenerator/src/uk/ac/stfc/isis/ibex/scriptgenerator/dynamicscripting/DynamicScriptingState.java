package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashMap;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus> {
	
	protected DynamicScriptingNicosFacade nicosFacade;
	protected DynamicScriptingModelFacade scriptGeneratorFacade;
	protected HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction;
	
	public DynamicScriptingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashMap<Integer, ScriptGeneratorAction> dynamicScriptIdsToAction) {
		this.nicosFacade = nicosFacade;
		this.scriptGeneratorFacade = generatorFacade;
		this.dynamicScriptIdsToAction = dynamicScriptIdsToAction;
	}
	
	public Boolean isScriptDynamic(Integer scriptId) {
		return dynamicScriptIdsToAction.containsKey(scriptId);
	}
	
	public HashMap<Integer, ScriptGeneratorAction> getClearScriptIdMap() {
		return new HashMap<Integer, ScriptGeneratorAction>();
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
		
	public abstract DynamicScriptingState stop();

}
