package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;


/**
 * Handles the configurations imported by the python interface.
 *
 */
public class ConfigLoader extends ModelObject {
	
	/**
	 * List of configs imported by the python interface.
	 */
	private List<Config> availableConfigs;
	
	/**
	 * The action parameters of the currently loaded config.
	 */
	private ArrayList<ActionParameter> parameters = new ArrayList<ActionParameter>();
	
	/**
	 * The currently selected config.
	 */
	private Config selectedConfig;
	
	/**
	 * The py4j interface handling the ConfigWrapper.
	 */
	private PythonInterface pythonInterface;	
	
	/**
	 * Constructor for the config loader, initialises the connection with python and reads the configurations.
	 */
	public ConfigLoader(PythonInterface pythonInterface) {
		this.pythonInterface = pythonInterface;
	    availableConfigs = pythonInterface.getActionDefinitions();
	    setConfig(availableConfigs.get(0));
	}

	/**
	 * @return all found configurations.
	 */
	public List<Config> getAvailableConfigs() {
		return availableConfigs;
	}

	/**
	 * Set which configuration is currently loaded.
	 * @param config The currently loaded configuration.
	 */
	public void setConfig(Config config) {
		ArrayList<ActionParameter> parameters = config.getParameters().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
		firePropertyChange("config", this.selectedConfig, this.selectedConfig = config);
	}
	
	/**
	 * @return the currently loaded configuration.
	 */
	public Config getConfig() {
		return selectedConfig;
	}
	
	/**
	 * @return the parameters for the current configuration.
	 */
	public ArrayList<ActionParameter> getParameters() {
		return parameters;
	}
	
	/**
	 * Cleans up all resources.
	 */
	public void cleanUp() {
		pythonInterface.cleanUp();
	}
}
