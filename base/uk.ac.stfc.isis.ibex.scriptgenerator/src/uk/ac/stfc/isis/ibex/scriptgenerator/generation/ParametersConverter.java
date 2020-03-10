package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.Map;

/**
 * Holds the content for converting user data to json format and also used for
 * holding content from json file.
 *
 */
public class ParametersConverter {

	/**
	 * Json format version.
	 */
	String versionJSONFormat;
	/**
	 * Script Generator version.
	 */
	String scriptGeneratorVersion;
	/**
	 * Date the data file is first saved.
	 */
	String date;
	/**
	 * Time the data file is first saved.
	 */
	String time;
	/**
	 * Script Definition File path
	 */
	String scriptDefinitionFilePath;
	/**
	 * Git hash of script definition file
	 */
	String scriptDefinitionFileGitHash;
	/**
	 * Genie python version number
	 */
	String geniePythonVersion;
	/**
	 * Content of script definition
	 */
	String scriptDefinitionContent;
	/**
	 * Parameter values mapped to its actions
	 */
	List<Map<String, String>> actions;
	
	/**
	 * Constructor to initialise member variables required for JSON to Java class and Java Class to JSON conversion.
	 * @param version Version of json format
	 * @param actions Actions from table
	 * @param content Content of script definition
	 * @param scriptGeneratorVersion Script generator version
	 * @param date Current date
	 * @param time Current time
	 * @param scriptDefinitionFilePath File path of script definition that is used to generate data file
	 * @param scriptDefinitionFileGitHash Git hash of Script defintion file 
	 * @param geniePythonVersion Current version of genie python
	 */
	public ParametersConverter(String version, List<Map<String, String>> actions, String content,
			String scriptGeneratorVersion, String date, String time, String scriptDefinitionFilePath,
			String scriptDefinitionFileGitHash, String geniePythonVersion) {
		this.versionJSONFormat = version;
		this.actions = actions;
		this.scriptDefinitionContent = content;
		this.scriptGeneratorVersion = scriptGeneratorVersion;
		this.date = date;
		this.time = time;
		this.scriptDefinitionFilePath = scriptDefinitionFilePath;
		this.scriptDefinitionFileGitHash = scriptDefinitionFileGitHash;
		this.geniePythonVersion = geniePythonVersion;
	}

	/**
	 * Gets parameter values i.e. user data
	 * 
	 * @return
	 */
	public List<Map<String, String>> getParameterValues() {
		return actions;
	}

	/**
	 * Gets version number of JSON format supported by us
	 * 
	 * @return version number
	 */
	public String getJSONFormatVersionNumber() {
		return versionJSONFormat;
	}

	/**
	 * Sets version number of JSON format
	 * 
	 * @param version version number
	 */
	public void setJSONFormatVersion(String version) {
		this.versionJSONFormat = version;
	}

	/**
	 * Sets Parameter Values
	 * 
	 * @param actionsAndParamValues Parameter values
	 */
	public void setParameterValues(List<Map<String, String>> actionsAndParamValues) {
		this.actions = actionsAndParamValues;
	}
	
	/**
	 * get the content of script definition file 
	 * @return content of script definition file
	 */
	public String getScriptDefinitionContent() {
		return scriptDefinitionContent;
	}

}
