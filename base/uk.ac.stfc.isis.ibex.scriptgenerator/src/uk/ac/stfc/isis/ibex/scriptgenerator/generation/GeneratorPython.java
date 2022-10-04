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
	
	/**
	 * Create a python generator with the specified python interface.
	 * Listen to changes in it's validity checking and generated script.
	 * 
	 * @param pythonInterface The py4j python interface to use to communicate with python.
	 */
	public GeneratorPython(PythonInterface pythonInterface) {
	
		this.pythonInterface = pythonInterface;
		this.pythonInterface.addPropertyChangeListener(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.VALIDITY_ERROR_MESSAGE_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
		this.pythonInterface.addPropertyChangeListener(ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.PARAM_VALIDITY_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
        this.pythonInterface.addPropertyChangeListener(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, evt -> {
            firePropertyChange(ScriptGeneratorProperties.TIME_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
        this.pythonInterface.addPropertyChangeListener(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, evt -> {
            firePropertyChange(ScriptGeneratorProperties.CUSTOM_ESTIMATE_PROPERTY, evt.getOldValue(), evt.getNewValue());
        });
		this.pythonInterface.addPropertyChangeListener(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, evt -> {
			firePropertyChange(ScriptGeneratorProperties.GENERATED_SCRIPT_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
	
	}
	
	@Override
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		try {	
			pythonInterface.refreshAreParamsValid(scriptGenContent, globalParams, scriptDefinition);
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}

	@Override
	public void refreshValidityErrors(List<String> globalParams, List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefintion) throws InterruptedException, ExecutionException {
		try {
			pythonInterface.refreshValidityErrors(globalParams, scriptGenContent, scriptDefintion);
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
    public void refreshCustomEstimation(List<ScriptGeneratorAction> scriptGenContent, ScriptDefinitionWrapper scriptDefinition, List<String> globalParams) throws InterruptedException, ExecutionException {
		try {
			pythonInterface.refreshCustomEstimation(scriptGenContent, scriptDefinition, globalParams);
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
