package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonNotReadyException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Use to generate a python script or check parameter validity from script generator contents.
 * 
 * @author James King
 *
 */
public class GeneratorPython extends AbstractProgrammingLanguageGenerator {
	
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
		this.pythonInterface.addPropertyChangeListener(GENERATED_SCRIPT_PROPERTY, evt -> {
			firePropertyChange(GENERATED_SCRIPT_PROPERTY, evt.getOldValue(), evt.getNewValue());
		});
	
	}

	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid against Python.
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call.
	 * @throws InterruptedException The Py4J call was interrupted.
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException {
		try {	
			pythonInterface.refreshAreParamsValid(scriptGenContent, config);
		} catch(PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}

	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call.
	 * @throws InterruptedException The Py4J call was interrupted.
	 */
	@Override
	public void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException {
		try {
			pythonInterface.refreshValidityErrors(scriptGenContent, config);
		} catch(PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
	}

	/**
	 * Method to generate python script
	 * @param scriptGenContent The script generator content to produce the script from
	 * @param currentlyLoadedDataFile data file that is currently loaded onto the script generator
	 * @param config The instrument config to generate the script with.
	 * @throws ExecutionException A failure to execute the py4j call.
	 * @throws InterruptedException The Py4J call was interrupted.
	 */
	public void generate(List<ScriptGeneratorAction> scriptGenContent, ParametersConverter currentlyLoadedDataFile, Config config)
			throws InterruptedException, ExecutionException {
		try {
			pythonInterface.refreshGeneratedScript(scriptGenContent, currentlyLoadedDataFile, config);
		} catch(PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening to python interface readiness changes (handled there)
			LOG.error(e);
		}
		
	}

}
