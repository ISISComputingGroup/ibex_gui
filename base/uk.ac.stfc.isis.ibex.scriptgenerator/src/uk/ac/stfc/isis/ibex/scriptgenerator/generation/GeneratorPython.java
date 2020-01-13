package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.PythonInterface;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Use to generate a python script or check parameter validity from script generator contents.
 * 
 * @author James King
 *
 */
public class GeneratorPython extends AbstractGenerator {
	
	private final PythonInterface pythonInterface;
	
	/**
	 * Create a python generator with the specified python interface.
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
	 * Refresh the generated python script property.
	 * 
	 * @param scriptGenContent The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @throws ExecutionException A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 */
	@Override
	public void refreshGeneratedScript(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException {
		pythonInterface.refreshGeneratedScript(scriptGenContent, config);
	}
	
	/**
	 * Refresh the property of whether the contents of the script generator (actionsTable) are valid against Python.
	 * 
	 * @param scriptGenContent The contents of the script generator to validate.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 */
	public void refreshAreParamsValid(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException {
		pythonInterface.refreshAreParamsValid(scriptGenContent, config);
	}

	/**
	 * Refresh the validity errors returned when checking validity.
	 * 
	 * @param scriptGenContent The contents of the script generator to check for validity errors with.
	 * @param config The instrument config to validate the script against.
	 * @throws ExecutionException A failure to execute the py4j call
	 * @throws InterruptedException The Py4J call was interrupted
	 */
	@Override
	public void refreshValidityErrors(List<ScriptGeneratorAction> scriptGenContent, Config config) throws InterruptedException, ExecutionException {
		pythonInterface.refreshValidityErrors(scriptGenContent, config);
	}
	
}
