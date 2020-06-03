package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.Logger;
import com.google.gson.annotations.SerializedName;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorActionConverter;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorFileHandler;

/**
 * Holds the content for converting user data to json format and also used for
 * holding content from json file.
 *
 */
public class ParametersConverter {
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorFileHandler.class);
	/**
	 * Json format version.
	 */
	@SerializedName("version_JSON_format")
	String versionJSONFormat;
	
	/**
	 * Script Generator version.
	 */
	@SerializedName("script_generator_version")
	String scriptGeneratorVersion;
	
	/**
	 * Date the data file is first saved.
	 */
	@SerializedName("date_and_time")
	String dateAndTime;
	
	/**
	 * Script Definition File path.
	 */
	@SerializedName("script_definition_file_path")
	String scriptDefinitionFilePath;
	
	/**
	 * Git hash of script definition file.
	 */
	@SerializedName("script_definition_file_git_hash")
	String scriptDefinitionFileGitHash;
	
	/**
	 * Genie python version number.
	 * */
	@SerializedName("genie_python_version")
	String geniePythonVersion;
	
	/**
	 * Content of script definition.
	 */
	@SerializedName("script_definition_content")
	String scriptDefinitionContent;
	
	/**
	 * All script Generator actions.
	 */
	List<ScriptGeneratorActionConverter> actions;
	
	
	/**
	 * Constructor to initialise member variables required for JSON to Java class and Java Class to JSON conversion.
	 * @param version Version of json format
	 * @param actions Actions from table
	 * @param content Content of script definition
	 * @param scriptGeneratorVersion Script generator version
	 * @param dateAndTime Current date and time
	 * @param scriptDefinitionFilePath File path of script definition that is used to generate data file
	 * @param scriptDefinitionFileGitHash Git hash of Script definition file 
	 * @param geniePythonVersion Current version of genie python
	 */
	public ParametersConverter(String version, List<ScriptGeneratorActionConverter> actions, String content,
			String scriptGeneratorVersion, String dateAndTime, String scriptDefinitionFilePath,
			String scriptDefinitionFileGitHash, String geniePythonVersion) {
		this.versionJSONFormat = version;
		this.actions = actions;
		this.scriptDefinitionContent = content;
		this.scriptGeneratorVersion = scriptGeneratorVersion;
		this.dateAndTime = dateAndTime;
		this.scriptDefinitionFilePath = scriptDefinitionFilePath;
		this.scriptDefinitionFileGitHash = scriptDefinitionFileGitHash;
		this.geniePythonVersion = geniePythonVersion;
	}

	/**
	 * Gets parameter values for an action i.e. user data.
	 * 
	 * @param actionName name of the action to get values for
	 * @return list of mapped parameter to values
	 */
	public List<Map<String, String>> getParameterValues(String actionName) {
		List<Map<String, String>> parameterValues = Collections.emptyList();
		for (ScriptGeneratorActionConverter action : this.actions) {
			if (action.getActionName().equals(actionName)) {
				parameterValues = action.getParameters(); 
			}
		}
		if (parameterValues.isEmpty()) {
		    LOG.error(String.format("Table name %s does not exist", actionName));
		}
		return parameterValues;
	}

	/**
	 * Gets version number of JSON format supported by us.
	 * 
	 * @return version number
	 */
	public String getJSONFormatVersionNumber() {
		return versionJSONFormat;
	}

	/**
	 * Sets version number of JSON format.
	 * 
	 * @param version version number
	 */
	public void setJSONFormatVersion(String version) {
		this.versionJSONFormat = version;
	}

	/**
	 * Sets Parameter Values.
	 * @param actionsAndParamValues Parameter values
	 */
	public void setParameterValues(List<ScriptGeneratorActionConverter> actionsAndParamValues) {
		this.actions = actionsAndParamValues;
	}
	
	/**
	 * Get the content of script definition file.
	 * @return content of script definition file
	 */
	public String getScriptDefinitionContent() {
		return scriptDefinitionContent;
	}

}
