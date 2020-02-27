package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.Map;
import com.google.gson.Gson;

/**
 * Holds the content for converting user data to json format and also used for
 * holding content from json file
 * 
 * @author mjq34833
 *
 */
public class ParametersConverter {

	String version;
	String scriptGeneratorVersion;
	String date;
	String time;
	String configurationFilePath;
	String configurationFileGitHash;
	String geniePythonVersion;
	String configurationContent;
	/**
	 * Parameter values mapped to its actions
	 */
	List<Map<String, String>> actions;
	
	/**
	 * Constructor to initialise member variables required for JSON to Java class and Java Class
	 * to JSON conversion
	 * @param version
	 * @param actions
	 * @param content
	 * @param scriptGeneratorVersion
	 * @param date
	 * @param time
	 * @param configurationPath
	 * @param configuraitonFileGitHash
	 * @param geniePythonVersion
	 */
	ParametersConverter(String version, List<Map<String, String>> actions, String content,
			String scriptGeneratorVersion, String date, String time, String configurationPath,
			String configuraitonFileGitHash, String geniePythonVersion) {
		this.version = version;
		this.actions = actions;
		this.configurationContent = content;
		this.scriptGeneratorVersion = scriptGeneratorVersion;
		this.date = date;
		this.time = time;
		this.configurationFilePath = configurationPath;
		this.configurationFileGitHash = configuraitonFileGitHash;
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
	 * Gets version number
	 * 
	 * @return version number
	 */
	public String getVersionNumber() {
		return version;
	}

	/**
	 * Sets version number
	 * 
	 * @param version version number
	 */
	public void setVersion(String version) {
		this.version = version;
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
	 * get the content of configuration file 
	 * @return content of configuration file
	 */
	public String getConfigContent() {
		return configurationContent;
	}

}
