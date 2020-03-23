package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.ParametersConverter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorActionConverter;

/**
 * Class to handle JSON file operations such as read/write, check for file duplicates.
 *
 */
public class ScriptGeneratorJsonFileHandler {
	
	/** 
	 * Currently supported JSON file version by us.
	 */
	public static String CURRENT_VERSION = "1";
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	private Gson gson = new GsonBuilder().setPrettyPrinting().create();
	/**
	 * Save Parameters to a JSON file.
	 * @param scriptGenContent content to generate data file with
	 * @param scriptDefPath path of script definition used to create data file
	 * @param absoluteFilePath absolute file path to save data file
	 */
	public void saveParameters(List<ScriptGeneratorAction> scriptGenContent, String scriptDefPath, String absoluteFilePath) throws InterruptedException, ExecutionException {
		try {
			
			String content = createJsonString(scriptGenContent, readFileContent(scriptDefPath), scriptDefPath);
			writeParametersToJSONFile(content, absoluteFilePath);

		} catch (IOException e) {
			LOG.error(e);
		}
	}
	
	/**
	 * Adds metadata and creates JSON string using script generator content.
	 * @param scriptGenContent content of script generator
	 * @param scriptDefCotnent content of script definition
	 * @param scriptDefPath path of script definition file
	 * @return JSON string 
	 * @throws IOException
	 */
	public String createJsonString(List<ScriptGeneratorAction> scriptGenContent, String scriptDefContent, String scriptDefPath) {
		
		List<ScriptGeneratorActionConverter> actions = new ArrayList<ScriptGeneratorActionConverter>();
		actions.add(new ScriptGeneratorActionConverter("default", convertScriptGenContent(scriptGenContent)));
		String jsonContent = gson.toJson(new ParametersConverter(CURRENT_VERSION, actions, scriptDefContent, "",
				getCurrentDateAndTime(), scriptDefPath, "", getGeniePythonVersion()));
		return jsonContent;
	}

	/**
	 * Load parameter values from a file only if the currently loaded script definition is the same as script definition in JSON file.
	 * @param filePath path of file to load parameter values from
	 * @param scriptDefinitionFilePath file path to script definition
	 * @throws ScriptDefinitionNotMatched script definition used to load and save the data file does not match
	 */
	public List<Map<ActionParameter, String>> getParameterValues(String filePath, String scriptDefinitionFilePath) throws ScriptDefinitionNotMatched,
	UnsupportedOperationException {
		List<Map<ActionParameter, String>> newMappedParameterToValues = Collections.emptyList();
		try {
			Optional<ParametersConverter> currentlyLoadedJSONFileContent = convertJSONtoObject(readFileContent(filePath));
				if (currentlyLoadedJSONFileContent.isPresent()) {
					String scriptDefStoredInJson = currentlyLoadedJSONFileContent.get().getScriptDefinitionContent();
					String currentlyLoadedScriptDef = readFileContent(scriptDefinitionFilePath);
				if (scriptDefStoredInJson.equals(currentlyLoadedScriptDef)) {
					// currently there is only one table so we just get parameters from default table
					List<Map<String, String>> mappedParamToValue =  currentlyLoadedJSONFileContent.get().getParameterValues("default");
					newMappedParameterToValues =  getMappedParamToValue(mappedParamToValue);
				} else {
					throw new ScriptDefinitionNotMatched("Current script definition does not match with the "
							+ "script definition that was used to generate the selected data file");
				}
			}
		} catch(JsonSyntaxException | JsonIOException | IOException e) {
			LOG.error(e);
		} 
		
		
		return newMappedParameterToValues;
	}
	
	/**
	 * Get Content from give filename. 
	 * @param jsonString json string to convert to an object
	 * @throws JsonSyntaxException JSON syntax incorrect
	 * @throws JsonIOException trouble reading JSON file
	 * @throws FileNotFoundException JSON file does not exist
	 * @return object representation of JSON which is read from a file
	 */
	private Optional<ParametersConverter> convertJSONtoObject(String jsonString) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		
		// Check if version number match
		try {
			ParametersConverter jsonContent = gson.fromJson(jsonString, ParametersConverter.class);
			if (jsonContent.getJSONFormatVersionNumber().equals(CURRENT_VERSION)) {
				return Optional.ofNullable(jsonContent);
			} else {
				LOG.error("Json " + jsonContent.getJSONFormatVersionNumber() + " version not supported");
				throw new UnsupportedOperationException(String.format("Data file version %s is not supported.\nSupported version %s"
						, jsonContent.getJSONFormatVersionNumber(), CURRENT_VERSION)); 
			}
		} catch (NullPointerException e) {
			LOG.error(e + ": Json version number is null");
		}
		return Optional.ofNullable(null);
	}
		
	/**
	 * Translate from List<Map<String, String>> to List<Map<ActionParameter, String>>.
	 * @param mappedParamToValue list of parameters mapped to its value
	 * @return List of Map<ActionParameter, String> values
	 */
	private List<Map<ActionParameter, String>> getMappedParamToValue(List<Map<String, String>> mappedParamToValue) {
		return  mappedParamToValue.stream().map(m -> m.entrySet().stream().collect(Collectors.toMap(p ->
			new ActionParameter(p.getKey()), p-> p.getValue()))).collect(Collectors.toList());
	}
	
	/**
	 * Generate JSON string from the parameters and add meta data.
	 * @param content content to write to JSON file
	 * @param absoluteFilePath absolute path to create a file and write to 
	 * @throws IOException 
	 */
	private void writeParametersToJSONFile(String content, String absoluteFilePath) 
			throws IOException {
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(absoluteFilePath));
		fileWriter.write(content);	
		fileWriter.flush();
		fileWriter.close();
	}
	
	/**
	 * Reads given file's content.
	 * @return string of JSON content
	 */
	 public String readFileContent(String filePath) throws IOException {
		String content = new String(Files.readAllBytes(Paths.get(filePath)));
		return content;
	}
	
	/**
	 * Convert script generator content to a map.
	 * @param scriptGenContent script generator content
	 * @return List of Mapped script generator content(action -> parameter)
	 */
	private List<Map<String, String>> convertScriptGenContent(List<ScriptGeneratorAction> scriptGenContent) {
		return scriptGenContent.stream()
				.map(action -> action.getAllActionParametersAsString()).collect(Collectors.toList());
	}

	  /**
     * Get current Date and Time. Access modifier is protected in order to override this method during unit test.
     * @return current date
     */
    protected String getCurrentDateAndTime() {
    	DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd HH:mm:ss");
    	Date date = new Date();
    	return dateFormat.format(date);
    }
    
    /**
     * Gets genie python version number. It is protected so that it can be overridden during unit test to avoid
     * run time error.
     * @return Genie python version number.
     */
    protected String getGeniePythonVersion() {
    	return Platform.getProduct().getDefiningBundle().getVersion().toString();
    }
 
}
