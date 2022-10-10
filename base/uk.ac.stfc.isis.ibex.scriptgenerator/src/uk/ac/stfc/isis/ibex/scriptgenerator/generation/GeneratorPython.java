package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;
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
	
	
	private void addListenerHelper(String property) {
		pythonInterface.addPropertyChangeListener(property, evt -> {
			firePropertyChange(property, evt.getOldValue(), evt.getNewValue());
		});
	}
	
	/**
	 * Create a python generator with the specified python interface.
	 * Listen to changes in it's validity checking and generated script.
	 * 
	 * @param pythonInterface The py4j python interface to use to communicate with python.
	 */
	public GeneratorPython(PythonInterface pythonInterface) {
		this.pythonInterface = pythonInterface;
		addListenerHelper(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY);
		addListenerHelper(ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY);
		addListenerHelper(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY);
		addListenerHelper(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY);
		addListenerHelper(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY);
	}
	
	private interface RefreshRunnable {
		void run() throws PythonNotReadyException, InterruptedException, ExecutionException;
	}
	
	private void runRefreshRunnable(RefreshRunnable runnable) throws InterruptedException, ExecutionException {
		try {
			runnable.run();
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}
	
	@Override
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		runRefreshRunnable(() -> {
			pythonInterface.refreshAreParamsValid(scriptGenContent, globalParams, scriptDefinition);
        });
	}

	@Override
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefintion) throws InterruptedException, ExecutionException {
		runRefreshRunnable(() -> {
			pythonInterface.refreshValidityErrors(globalParams, scriptGenContent, scriptDefintion);
        });
	}

	@Override
	public void refreshTimeEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		runRefreshRunnable(() -> {
			pythonInterface.refreshTimeEstimation(scriptGenContent, scriptDefinition, globalParams);
        });
	}
	
	@Override
    public void refreshCustomEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		runRefreshRunnable(() -> {
			pythonInterface.refreshCustomEstimation(scriptGenContent, scriptDefinition, globalParams);
        });
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
