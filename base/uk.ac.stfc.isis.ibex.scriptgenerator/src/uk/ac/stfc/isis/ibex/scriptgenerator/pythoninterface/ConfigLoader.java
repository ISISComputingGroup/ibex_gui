package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
	 * Map of configs which could not be loaded and the reason.
	 */
	private Map<String, String> configLoadErrors;
	
	/**
	 * The action parameters of the currently loaded config.
	 */
	private ArrayList<ActionParameter> parameters = new ArrayList<ActionParameter>();
	
	/**
	 * The currently selected config.
	 */
	private Optional<Config> selectedConfig = Optional.empty();
	
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
	    configLoadErrors = pythonInterface.getConfigLoadErrors();
	    if(!availableConfigs.isEmpty()) {
	    	setConfig(availableConfigs.get(0));
	    }
	}

	/**
	 * @return all found configurations.
	 */
	public List<Config> getAvailableConfigs() {
		return availableConfigs;
	}
	
	/**
	 * Gets all actions that could not be loaded and the reason.
	 */
	public Map<String, String> getConfigLoadErrors() {
		return configLoadErrors;
	}

	/**
	 * Set which configuration is currently loaded.
	 * @param config The currently loaded configuration.
	 */
	public void setConfig(Config config) {
		ArrayList<ActionParameter> parameters = config.getParameters().stream()
				.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
		firePropertyChange("parameters", this.parameters, this.parameters=parameters);
		this.selectedConfig = Optional.ofNullable(config);
		firePropertyChange("config", null, null);
	}
	
	/**
	 * @return The currently loaded config.
	 */
	public Config getConfig() {
		return selectedConfig.get();
	}
	
	/**
	 * @return An optional of the currently loaded configuration.
	 */
	public Optional<Config> getOptionalConfig() {
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
	
	/**
	 * Check if there are any configs loaded
	 * 
	 * @return true if there is at least one config loaded, false if not.
	 */
	public boolean configsLoaded() {
		return ! getAvailableConfigs().isEmpty();
	}
}
