package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import py4j.Py4JException;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
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
	 * The name of the last selected config.
	 */
	private Optional<String> lastSelectedConfigName = Optional.empty();
	
	/**
	 * The py4j interface handling the ConfigWrapper.
	 */
	private PythonInterface pythonInterface;	
	
	/**
	 * Denotes whether configs have been loaded successfully.
	 */
	private boolean configsLoaded = false;
	
	private static final Logger LOG = IsisLog.getLogger(ConfigLoader.class);
	
	/**
	 * Constructor for the config loader, initialises the connection with python and reads the configurations.
	 */
	public ConfigLoader(PythonInterface pythonInterface) {
		this.pythonInterface = pythonInterface;
		reloadConfigs();
	}
	
	/**
	 * Reload the configs 
	 */
	public void reloadConfigs() {
		 try {
			availableConfigs = pythonInterface.getActionDefinitions();
			configLoadErrors = pythonInterface.getConfigLoadErrors();
			configsLoaded = true;
			if(!availableConfigs.isEmpty()) {
				lastSelectedConfigName.ifPresentOrElse(configName -> {
					setConfig(configName);
				}, () -> setConfig(availableConfigs.get(0)));
		    }
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening for python readiness changes, handled there
			LOG.error(e);
			availableConfigs = new ArrayList<>();
			configLoadErrors = new HashMap<>();
			configsLoaded = false;
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
	 * Set the selected config based on the name of the config.
	 * 
	 * @param configName The name of the config to select.
	 */
	public void setConfig(String configName) {
		lastSelectedConfigName = Optional.of(configName);
		try {
			for(Config config : getAvailableConfigs()) {
				if(config.getName().equals(configName)) {
					setConfig(config);
					return;
				}
			}
			if(!availableConfigs.isEmpty()) {
				LOG.error("No config matching name: " + configName + ". Setting default config.");
				setConfig(getAvailableConfigs().get(0));
			} else {
				LOG.error("No default config available");
			}
		} catch(Py4JException e) {
			LOG.error(e);
			pythonInterface.handlePythonReadinessChange(false);
		}
	}

	/**
	 * Set which configuration is currently loaded.
	 * @param config The currently loaded configuration.
	 */
	public void setConfig(Config config) {
		try {
			ArrayList<ActionParameter> parameters = config.getParameters().stream()
					.map(name -> new ActionParameter(name)).collect(Collectors.toCollection(ArrayList::new));
			firePropertyChange("parameters", this.parameters, this.parameters=parameters);
			selectedConfig = Optional.ofNullable(config);
			selectedConfig.ifPresentOrElse(presentSelectedConfig -> {
				lastSelectedConfigName = Optional.of(presentSelectedConfig.getName());
			}, () -> lastSelectedConfigName = Optional.empty());
			firePropertyChange("config", null, null);
		} catch(Py4JException e) {
			LOG.error(e);
			pythonInterface.handlePythonReadinessChange(false);
		}
	}
	
	/**
	 * @return The currently loaded config.
	 */
	public Config getConfig() {
		return selectedConfig.get();
	}
	
	/**
	 * @return The name of the last selected config.
	 */
	public Optional<String> getLastSelectedConfigName() {
		return lastSelectedConfigName;
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
	 * Check whether configs have finished loading.
	 * 
	 * @return true if configs have loaded successfully, false if not.
	 */
	public boolean configsLoaded() {
		return configsLoaded;
	}
	
	/**
	 * Check if there are any configs loaded
	 * 
	 * @return true if there is at least one config loaded, false if not.
	 */
	public boolean configsAvailable() {
		return ! getAvailableConfigs().isEmpty();
	}
}
