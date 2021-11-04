package uk.ac.stfc.isis.ibex.scriptgenerator.dynamicscripting;

import java.util.Optional;

import uk.ac.stfc.isis.ibex.nicos.NicosModel;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

public abstract class DynamicScriptingState {
	
	ScriptGeneratorSingleton scriptGeneratorModel;
	NicosModel nicosModel;
	
	public DynamicScriptingState(ScriptGeneratorSingleton scriptGeneratorModel, NicosModel nicosModel) {
		this.scriptGeneratorModel = scriptGeneratorModel;
		this.nicosModel = nicosModel;
	}
	
	public abstract Optional<ScriptGeneratorAction> getCurrentlyExecutingAction();
	
	public abstract Optional<ScriptGeneratorAction> getNextExecutingAction();
	
	public abstract DynamicScriptingStatus getStatus();
	
	public abstract DynamicScriptingState play();
	
	public abstract DynamicScriptingState pause();
	
	public abstract DynamicScriptingState stop();

}
