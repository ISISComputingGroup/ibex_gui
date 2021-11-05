package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.HasStatus;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject implements HasStatus<DynamicScriptingStatus> {
	
	protected ScriptGeneratorSingleton scriptGeneratorModel;
	protected NicosModel nicosModel;
	protected DynamicScriptingNicosFacade nicosFacade;
	protected DynamicScriptingGeneratorFacade generatorFacade;
	
	public DynamicScriptingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade, DynamicScriptingGeneratorFacade generatorFacade) {
		this.scriptGeneratorModel = scriptGeneratorModel;
		this.nicosModel = nicosModel;
		this.nicosFacade = nicosFacade;
		this.generatorFacade = generatorFacade;
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
		
	public abstract DynamicScriptingState stop();

}
