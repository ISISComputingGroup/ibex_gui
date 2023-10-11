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
import uk.ac.stfc.isis.ibex.scriptgenerator.JavaActionParameter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorProperties;


/**
 * Handles the script definitions imported by the python interface.
 *
 */
public class ScriptDefinitionLoader extends ModelObject {
	
	/**
	 * List of script definitions imported by the python interface.
	 */
	private List<ScriptDefinitionWrapper> availableScriptDefinitions;
	
	/**
	 * Map of script definitions which could not be loaded and the reason.
	 */
	private Map<String, String> scriptDefinitionLoadErrors;
	
	/**
	 * The action parameters of the currently loaded script definition.
	 */
	private ArrayList<JavaActionParameter> parameters = new ArrayList<JavaActionParameter>();
	
	/**
	 * The currently selected script definition.
	 */
	private Optional<ScriptDefinitionWrapper> selectedScriptDefinition = Optional.empty();
	
	/**
	 * The name of the last selected script definition.
	 */
	private Optional<String> lastSelectedScriptName = Optional.empty();
	
	/**
	 * The py4j interface handling the ScriptDefinitionsWrapper.
	 */
	private PythonInterface pythonInterface;	
	
	/**
	 * Denotes whether script definition have been loaded successfully.
	 */
	private boolean scriptDefinitionsLoaded = false;
	
	private static final Logger LOG = IsisLog.getLogger(ScriptDefinitionLoader.class);
	
	/**
	 * Constructor for the script definition loader, initialises the connection with python and reads the script definitions.
	 * 
	 * @param pythonInterface The python interface to send python commands and queries through.
	 */
	public ScriptDefinitionLoader(PythonInterface pythonInterface) {
		this.pythonInterface = pythonInterface;
		reloadScriptDefinitions();
	}
	
	/**
	 * Reload the script definitions.
	 */
	public void reloadScriptDefinitions() {
		 try {
			availableScriptDefinitions = pythonInterface.getScriptDefinitions();
			scriptDefinitionLoadErrors = pythonInterface.getScriptDefinitionLoadErrors();
			scriptDefinitionsLoaded = true;
			if (!availableScriptDefinitions.isEmpty()) {
				lastSelectedScriptName.ifPresentOrElse(scriptDefinitionName -> {
					setScriptDefinition(scriptDefinitionName);
				}, () -> setScriptDefinition(availableScriptDefinitions.get(0)));
		    }
		} catch (PythonNotReadyException e) {
			// ScriptGeneratorSingleton is listening for python readiness changes, handled there
			LOG.error(e);
			availableScriptDefinitions = new ArrayList<>();
			scriptDefinitionLoadErrors = new HashMap<>();
			scriptDefinitionsLoaded = false;
		}
	}

	/**
	 * @return all found script definitions.
	 */
	public List<ScriptDefinitionWrapper> getAvailableScriptDefinitions() {
		return availableScriptDefinitions;
	}
	
	/**
	 * Gets all script definitions that could not be loaded and the reason.
	 * 
	 * @return Any errors when loading script definitions,
	 *  where the key is the script definition name and the value is the reason it could not be loaded.
	 */
	public Map<String, String> getScriptDefinitionLoadErrors() {
		return scriptDefinitionLoadErrors;
	}
	
	/**
	 * Set the selected script definition based on the name of the script definition.
	 * 
	 * @param scriptDefinitionName The name of the script definition to select.
	 */
	public void setScriptDefinition(String scriptDefinitionName) {
		lastSelectedScriptName = Optional.of(scriptDefinitionName);
		try {
			for (ScriptDefinitionWrapper scriptDefinition : getAvailableScriptDefinitions()) {
				if (scriptDefinition.getName().equals(scriptDefinitionName)) {
					setScriptDefinition(scriptDefinition);
					return;
				}
			}
			if (!availableScriptDefinitions.isEmpty()) {
				LOG.error("No script definition matching name: " + scriptDefinitionName + ". Setting default script definition.");
				setScriptDefinition(getAvailableScriptDefinitions().get(0));
			} else {
				LOG.error("No default script definition available");
			}
		} catch (Py4JException e) {
			LOG.error(e);
			pythonInterface.handlePythonReadinessChange(false);
		}
	}

	/**
	 * Set which script definitions is currently loaded.
	 * @param scriptDefinition The currently loaded script definitions.
	 */
	public void setScriptDefinition(ScriptDefinitionWrapper scriptDefinition) {
		try {
			ArrayList<JavaActionParameter> parameters = scriptDefinition.getParameters().stream()
					.map(param_details -> new JavaActionParameter(param_details.getName(), param_details.getDefaultValue(), param_details.getCopyPreviousRow(), param_details.getIsEnum(), param_details.getEnumValues()))
					.collect(Collectors.toCollection(ArrayList::new));
			firePropertyChange(ScriptGeneratorProperties.PARAMETERS_PROPERTY, this.parameters, this.parameters = parameters);
			selectedScriptDefinition = Optional.ofNullable(scriptDefinition);
			selectedScriptDefinition.ifPresentOrElse(presentSelectedScriptDefinition -> {
				lastSelectedScriptName = Optional.of(presentSelectedScriptDefinition.getName());
			}, () -> lastSelectedScriptName = Optional.empty());
			firePropertyChange(ScriptGeneratorProperties.SCRIPT_DEFINITION_SWITCH_PROPERTY, null, null);
		} catch (Py4JException e) {
			LOG.error(e);
			pythonInterface.handlePythonReadinessChange(false);
		}
	}
	
	/**
	 * @return The currently loaded script definition.
	 */
	public ScriptDefinitionWrapper getScriptDefinition() {
		return selectedScriptDefinition.get();
	}
	
	/**
	 * @return The name of the last selected script definition.
	 */
	public Optional<String> getLastSelectedScriptDefinitionName() {
		return lastSelectedScriptName;
	}
	
	/**
	 * @return the parameters for the current script definition.
	 */
	public ArrayList<JavaActionParameter> getParameters() {
		return parameters;
	}
	
	/**
	 * Cleans up all resources.
	 */
	public void cleanUp() {
		pythonInterface.cleanUp();
	}
	
	/**
	 * Check whether script definitions have finished loading.
	 * 
	 * @return true if script definitions have loaded successfully, false if not.
	 */
	public boolean scriptDefinitionsLoaded() {
		return scriptDefinitionsLoaded;
	}
	
	/**
	 * Check if there are any script definitions loaded.
	 * 
	 * @return true if there is at least one script definition loaded, false if not.
	 */
	public boolean scriptDefinitionAvailable() {
		return !getAvailableScriptDefinitions().isEmpty();
	}

	/**
	 * Gets the path to the script definitions repository.
	 * @return The path to the script definitions repository.
	 */
	public String getRepoPath() {
		return pythonInterface.getRepoPath();
	}
}
