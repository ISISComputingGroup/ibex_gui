package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
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
	 * Refresh the generated script property with a script (String).
	 * 
	 * @param scriptGenContent The script generator content to produce the script from.
	 * @param scriptDefinition The instrument script definition to generate the script with.
	 * @param currentlyLoadedDataFileContent the data file that user has currently loaded to generate script
	 * @param globalParams The global parameters to generate the script with.
	 * @return An optional ID of the script that will be generated, the ID can be used to get the generated script once generated.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract Optional<Integer> refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, String currentlyLoadedDataFileContent, List<String> globalParams) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid (bool).
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param scriptDefinition The instrument script definition to validate the script against.
	 * @param globalParams The global parameters to check parameter validity.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException;
	
	/**
	 * Refresh the validity errors returned when checking validity (Map<Integer,String>).
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param scriptDefinition The instrument script definition to validate the script against.
	 * @param globalParams The global parameters to check parameter validity.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	public abstract void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition) throws InterruptedException, ExecutionException;
    
    /**
     * Refresh the time estimation of the specified actions (Map<Integer,Double>).
     * 
     * @param scriptGenContent The contents of the script generator
     * @param scriptDefinition The instrument script definition
     * @param globalParams The global parameters to refresh time estimation with.
     * @throws ExecutionException A failure to execute the call to generate a script
     * @throws InterruptedException The call to generate a script was interrupted
     */
    public abstract void refreshTimeEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException;

    /**
     * Get the generated script from the given ID.
     * 
     * @param scriptId The ID of the script to get.
     * @return The script
     */
    public abstract Optional<String> getScriptFromId(Integer scriptId);
}
