package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * An interface to be implemented when generating and checking the parameter validity of the script generator's contents.
 * Those that extend this class should not manipulate the contents of the script generator, they should only access the contents.
 * The reason that the objects aren't passed from GeneratorContext as immutable is due to possible limitations in Py4J not being
 *   able to handle those immutable objects.
 * Those that extend this class should implement methods that run in a separate thread to the GUI thread. Examples of this can be
 *   seen in the way GeneratorPython calls into PythonInterface are run as CompletableFutures in a new thread. Once the future is
 *   complete then the relevant property is updated and a property change fired which will be recognised by the GeneratorContext.
 * 
 * @author James King
 *
 */
public abstract class AbstractGenerator extends ModelObject {
	
	/**
	 * The property to fire a change of when the validity error messages (Map<Integer, String>)
	 *  have been retrieved. This will get caught by the GeneratorContext and dealt with up the chain.
	 * Fire the change with the new value as the retrieved value.
	 */
	protected static final String VALIDITY_ERROR_MESSAGE_PROPERTY = "validity error messages";
	
	/**
	 * The property to fire a change of when the validity of the script generator content is
	 *  retrieved (bool). This will get caught by the GeneratorContext and dealt with up the chain.
	 * Fire the change with the new value as the retrieved value.
	 */
	protected static final String PARAM_VALIDITY_PROPERTY = "parameter validity";
	
	/**
	 * The property to fire a change of when the generated script is retrieved (String).
	 * This will get caught by the GeneratorContext and dealt with up the chain.
	 * Fire the change with the new value as the retrieved value.
	 */
	protected static final String GENERATED_SCRIPT_PROPERTY = "generated script";
	
	/**
	 * Refresh the generated script property with a script (String).
	 * 
	 * @param scriptGenContent The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid (bool).
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the validity errors returned when checking validity (Map<Integer,String>).
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException;
	
}