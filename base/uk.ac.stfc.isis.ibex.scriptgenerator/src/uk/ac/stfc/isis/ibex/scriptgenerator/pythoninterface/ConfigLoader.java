package uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ActionParameter;

public class ConfigLoader extends ModelObject {
	
	private static final Logger LOG = IsisLog.getLogger(ConfigLoader.class);
	
	private List<Config> availableConfigs;
	private ArrayList<ActionParameter> parameters = new ArrayList<ActionParameter>();
	private Config selectedConfig;

	private PythonInterface pythonInterface;	
	
	/**
	 * Constructor for the config loader, initialises the connection with python and reads the configurations.
	 */
	public ConfigLoader(PythonInterface pythonInterface) {
        try {
        	this.pythonInterface = pythonInterface;
        	pythonInterface.setUpPythonThread();
        	availableConfigs = pythonInterface.getActionDefinitions();
            setConfig(availableConfigs.get(0));
		} catch (IOException e) {
			LOG.error("ConfigLoader could not start");
			LOG.error(e);
		}
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
