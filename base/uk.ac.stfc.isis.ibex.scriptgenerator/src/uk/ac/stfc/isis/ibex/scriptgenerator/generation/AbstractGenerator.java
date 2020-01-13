package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * An interface to be implemented by each different language for the script generator.
 * 
 * @author James King
 *
 */
public abstract class AbstractGenerator extends ModelObject {
	
	protected static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	protected static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	protected static final String GENERATED_SCRIPT_PROPERTY = "generated script";
	
	/**
	 * Refresh the generated script in the generator.
	 * 
	 * @param scriptGenContent The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid.
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
}
