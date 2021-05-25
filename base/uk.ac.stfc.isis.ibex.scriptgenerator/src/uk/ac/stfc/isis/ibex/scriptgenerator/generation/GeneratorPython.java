package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonNotReadyException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Use to generate a python script or check parameter validity from script generator contents.
 * 
 * @author James King
 *
 */
public class GeneratorPython extends AbstractGenerator {
	
	private final PythonInterface pythonInterface;
	
	private static final Logger LOG = IsisLog.getLogger(GeneratorPython.class);
	
	/**
	 * Create a python generator with the specified python interface.
	 * Listen to changes in it's validity checking and generated script.
	 * 
	 * @param pythonInterface The py4j python interface to use to communicate with python.
	 */
	public GeneratorPython(PythonInterface pythonInterface) {
	
		this.pythonInterface = pythonInterface;
		this.pythonInterface.addPropertyChangeListener(VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			firePropertyChange(VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		this.pythonInterface.addPropertyChangeListener(PARAM_VALIDITY_PROPERTY, evt -> {
			firePropertyChange(PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
        this.pythonInterface.addPropertyChangeListener(TIME_ESTIMATE_PROPERTY, evt -> {
            firePropertyChange(TIME_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
		this.pythonInterface.addPropertyChangeListener(GENERATED_SCRIPT_PROPERTY, evt -> {
			firePropertyChange(GENERATED_SCRIPT_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
	
	}
	
	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid against Python.
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param scriptDefinition The script definition to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call.
	 * @throws InterruptedException The Py4J call was interrupted.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		try {	
			pythonInterface.refreshAreParamsValid(scriptGenContent, globalParams, scriptDefinition);
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}

	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param scriptDefintion The script definition to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call.
	 * @throws InterruptedException The Py4J call was interrupted.
	 */
	@Override
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefintion) throws InterruptedException, ExecutionException {
		try {
			pythonInterface.refreshValidityErrors(globalParams,scriptGenContent, scriptDefintion);
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}

	@Override
	public void refreshTimeEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
        try {
            pythonInterface.refreshTimeEstimation(scriptGenContent, scriptDefinition, globalParams);
        } catch (PythonNotReadyException e) {
            // ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
            LOG.error(e);
        }
	}

	@Override
	public Optional<Integer> refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent,
			ScriptDefinitionWrapper scriptDefinition, String jsonContent, List<String> globalParams) throws InterruptedException, ExecutionException {
		try {
			return Optional.of(pythonInterface.refreshGeneratedScript(scriptGenContent, jsonContent, globalParams, scriptDefinition));
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
			return Optional.empty();
		}
		
	}

	@Override
	public Optional<String> getScriptFromId(Integer scriptId) {
		return pythonInterface.getScriptFromId(scriptId);
	}
}
