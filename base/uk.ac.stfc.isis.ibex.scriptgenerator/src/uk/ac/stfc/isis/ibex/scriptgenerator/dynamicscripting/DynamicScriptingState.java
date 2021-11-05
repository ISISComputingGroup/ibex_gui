package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState extends ModelObject {
	
	protected static final String STATE_CHANGE_PROPERTY = "stateChange";
	
	protected ScriptGeneratorSingleton scriptGeneratorModel;
	protected NicosModel nicosModel;
	protected DynamicScriptingNicosFacade nicosFacade;
	
	public DynamicScriptingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel, DynamicScriptingNicosFacade nicosFacade) {
		this.scriptGeneratorModel = scriptGeneratorModel;
		this.nicosModel = nicosModel;
		this.nicosFacade = nicosFacade;
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
	
	public abstract DynamicScriptingState pause();
	
	public abstract DynamicScriptingState stop();

}
