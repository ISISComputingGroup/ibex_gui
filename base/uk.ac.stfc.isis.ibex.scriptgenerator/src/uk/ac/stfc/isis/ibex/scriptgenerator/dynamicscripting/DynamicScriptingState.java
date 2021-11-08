package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus> {
	
	protected DynamicScriptingNicosFacade nicosFacade;
	protected DynamicScriptingModelFacade scriptGeneratorFacade;
	
	public DynamicScriptingState(DynamicScriptingNicosFacade nicosFacade, DynamicScriptingModelFacade generatorFacade) {
		this.nicosFacade = nicosFacade;
		this.scriptGeneratorFacade = generatorFacade;
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
		
	public abstract DynamicScriptingState stop();

}
