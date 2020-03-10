package uk.ac.stfc.isis.ibex.scriptgenerator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.ParametersConverter;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Class to handle JSON file operations such as read/write, check for file duplicates.
 *
 */
public class ScriptGeneratorJsonFileHandler {
	/** 
	 * Currently supported JSON file version by us.
	 */
	public static String CURRENT_VERSION = "1";
	private final PreferenceSupplier preferenceSupplier = new PreferenceSupplier();
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	private ParametersConverter currentlyLoadedJSONFileContent = null;
	
	

	/**
	 * Get list of data files from the directory where data files are saved.
	 * @return list of files 
	 * @throws FileNotFoundException when DataFiles folder does not exist
	 */
	public List<String> getListOfdataFiles() throws FileNotFoundException {
		List<String> list = new ArrayList<String>();
		if (Files.isDirectory(Paths.get(preferenceSupplier.scriptGeneratorDataFileFolder()))) {
			File folder = new File(getScriptGenDataFileFolder());
			list = Arrays.asList(folder.list((dir, name) ->(name.endsWith(".json"))));
			// remove extension
			for (ListIterator<String> itr = list.listIterator(); itr.hasNext();) {
				list.set(itr.nextIndex(), itr.next().replace(".json", ""));
			}
			
		} else {
			throw new FileNotFoundException();
		}
		return list;
	}

	private String getScriptGenDataFileFolder() {
		return preferenceSupplier.scriptGeneratorDataFileFolder();
	}
	
	/**
	 * Save Parameters to a JSON file.
	 * @param scriptGenContent content to generate data file with
	 * @param scriptDefPath path of script definition used to create data file
	 * @param absoluteFilePath absolute file path to save data file
	 */
	public void saveParameters(List<ScriptGeneratorAction> scriptGenContent, String scriptDefPath, String absoluteFilePath) throws InterruptedException, ExecutionException {
		try {
			// scriptDefinition content
			String content = new String(Files.readAllBytes(Paths.get(scriptDefPath)));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String jsonContent = gson.toJson(new ParametersConverter(CURRENT_VERSION, convertScriptGenContent(scriptGenContent), content, "",
					getCurrentDate(), getCurrentTime(), scriptDefPath, "", getCurrentGenieVersionNumber()));
			writeParametersToJSONFile(jsonContent, absoluteFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Get current version number of genie python.
	 * @return current genie python version number
	 */
	public String getCurrentGenieVersionNumber() {
		return Platform.getProduct().getDefiningBundle().getVersion().toString();
	}
	/**
	 * Load parameter values from a file only if the currently loaded script definition is the same as script definition in JSON file.
	 * @param filePath path of file to load parameter values from
	 * @param scriptDefinitionFilePath file path to script definition
	 * @throws ScriptDefinitionNotMatched script definition used to load and save the data file does not match
	 */
	public List<Map<ActionParameter, String>> getParameterValues(String filePath, String scriptDefinitionFilePath) throws ScriptDefinitionNotMatched,
	UnsupportedOperationException {
		List<Map<ActionParameter, String>> newMappedParameterToValues = null;
		try {
			this.currentlyLoadedJSONFileContent = convertJSONtoObject(filePath);
			String scriptDefStoredInJson = currentlyLoadedJSONFileContent.getScriptDefinitionContent();
			String actualScriptDefcontent = new String(
					Files.readAllBytes(Paths.get(scriptDefinitionFilePath)));
			if (scriptDefStoredInJson.equals(actualScriptDefcontent)) {
				List<Map<String, String>> mappedParamToValue =  currentlyLoadedJSONFileContent.getParameterValues();
				newMappedParameterToValues =  getMappedParamToValue(mappedParamToValue);
			} else {
				throw new ScriptDefinitionNotMatched("Current script definition does not match with the "
						+ "script definition that was used to generate the selected data file");
			}
		} catch(JsonSyntaxException | JsonIOException | IOException e) {
			LOG.error(e);
		} 
		
		return newMappedParameterToValues;
	}
	
	/**
	 * Content of data file that is currently loaded by user.
	 * @return content of currently loaded data file in string format.
	 */
	public String getCurrentlyLoadedDataFileContent() {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(currentlyLoadedJSONFileContent);
	}
	
	/**
	 * Get Content from give filename.
	 * @return 
	 * filePath path of file to get content from
	 * @throws JsonSyntaxException JSON syntax incorrect
	 * @throws JsonIOException trouble reading JSON file
	 * @throws FileNotFoundException JSON file does not exist
	 */
	private ParametersConverter convertJSONtoObject(String filePath) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		
		// Check if version number match
		try {
			Gson gson = new Gson();
			FileReader reader = new FileReader(filePath);
			ParametersConverter jsonContent = gson.fromJson(reader, ParametersConverter.class);
			reader.close();
			if (jsonContent.getJSONFormatVersionNumber().equals(CURRENT_VERSION)) {
				return jsonContent;
			} else {
				LOG.error("Json " + jsonContent.getJSONFormatVersionNumber() + " version not supported");
				throw new UnsupportedOperationException(String.format("Data file version %s is not supported.\nSupported version %s"
						, jsonContent.getJSONFormatVersionNumber(), CURRENT_VERSION)); 
			}
		} catch (NullPointerException e) {
			LOG.error(e + ": Json version number is null");
		} catch (IOException e) {
			LOG.error(e);
		}
		
		return null;
	}
		
	/**
	 * Translate from List<Map<String, String>> to List<Map<ActionParameter, String>>.
	 * @param mappedParamToValue list of parameters mapped to its value
	 * @return List of Map<ActionParameter, String> values
	 */
	private ArrayList<Map<ActionParameter, String>> getMappedParamToValue(List<Map<String, String>> mappedParamToValue) {
		ArrayList<Map<ActionParameter, String>> newMappedParamToValue = new ArrayList<Map<ActionParameter, String>>();
		for (Map<String, String> map : mappedParamToValue) {
			Map<ActionParameter, String> tempMap = new HashMap<ActionParameter, String>();
			for (Map.Entry<String, String> entry : map.entrySet()) {
				tempMap.put(new ActionParameter(entry.getKey()), entry.getValue());
			}
			newMappedParamToValue.add(tempMap);
		}
		return newMappedParamToValue;
	}
	
	/**
	 * Generate JSON string from the parameters and add meta data.
	 * @param content content to write to JSON file
	 * @param absoluteFilePath absolute path to create a file and write to 
	 * @throws IOException 
	 */
	private void writeParametersToJSONFile(String content, String absoluteFilePath) 
			throws IOException {
		@SuppressWarnings("resource")
		BufferedWriter fileWriter = new BufferedWriter(new FileWriter(absoluteFilePath));
		fileWriter.write(content);	
		fileWriter.flush();
		fileWriter.close();
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
     * Get current Date.
     * @return current date
     */
    private String getCurrentDate() {
    	DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
    	Date date = new Date();
    	return dateFormat.format(date);
    }
    
    /**
     * Get current time.
     * @return current time
     */
    private String getCurrentTime() {
    	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	Date date = new Date();
    	return dateFormat.format(date);
    }


}
