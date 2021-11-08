package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.HashSet;
import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus> {
	
	protected DynamicScriptingNicosFacade nicosFacade;
	protected DynamicScriptingModelFacade scriptGeneratorFacade;
	protected HashSet<Integer> dynamicScriptIds;
	
	public DynamicScriptingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade, HashSet<Integer> dynamicScriptIds) {
		this.nicosFacade = nicosFacade;
		this.scriptGeneratorFacade = generatorFacade;
		this.dynamicScriptIds = dynamicScriptIds;
	}
	
	public Boolean isScriptDynamic(Integer scriptId) {
		return dynamicScriptIds.contains(scriptId);
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
		
	public abstract DynamicScriptingState stop();

}
