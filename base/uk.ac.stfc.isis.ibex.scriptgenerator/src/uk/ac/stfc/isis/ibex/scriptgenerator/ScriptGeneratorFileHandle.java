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
 * @author Bishal Rai
 *
 */
public class ScriptGeneratorFileHandle {
	/** 
	 * Currently supported JSON file version by us.
	 */
	private String CURRENT_VERSION = "1";
	private final PreferenceSupplier preferenceSupplier = new PreferenceSupplier();
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	private ParametersConverter currentlyLoadedDataFileContent = null;
	

	/**
	 * Get list of data files from the directory where data files are saved.
	 * @return list of files 
	 * @throws FileNotFoundException when DataFiles folder does not exist
	 */
	public List<String> getListOfdataFiles() throws FileNotFoundException {
		List<String> list = new ArrayList<String>();
		
		if (Files.isDirectory(Paths.get(preferenceSupplier.scriptGeneratorDataFileFolder()))) {
			File folder = new File(preferenceSupplier.scriptGeneratorDataFileFolder());
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
	
	/**
	 * Save Parameters to a JSON file.
	 * @param scriptGenContent content to generate data file with
	 * @param scriptDefPath path of script definition used to create data file
	 * @param absoluteFilePath absolute file path to save data file
	 */
	public void saveParameters(List<ScriptGeneratorAction> scriptGenContent, String scriptDefPath, String absoluteFilePath) throws InterruptedException, ExecutionException {
		try {
			// scriptDefinitionConfig content
			String content = new String(Files.readAllBytes(Paths.get(scriptDefPath)));
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			String version = Platform.getProduct().getDefiningBundle().getVersion().toString();
			String jsonContent = gson.toJson(new ParametersConverter(CURRENT_VERSION, convertScriptGenContent(scriptGenContent), content, "",
					getCurrentDate(), getCurrentTime(), scriptDefPath, "", version));
			writeParametersToJSONFile(jsonContent, absoluteFilePath);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
	 * Load parameter values from a file.
	 * @param filePath path of file to load parameter values from
	 * @throws ScriptDefinitionNotMatched 
	 */
	public List<Map<ActionParameter, String>> getParameterValues(String filePath, String scriptDefinitionName) throws ScriptDefinitionNotMatched {
		List<Map<ActionParameter, String>> newMappedParameterToValues = null;
		try {
			this.currentlyLoadedDataFileContent = readParametersFromJSONFile(filePath);
			String configStoredInJson = currentlyLoadedDataFileContent.getScriptDefinitionContent();
			String actualConfigcontent = new String(
					Files.readAllBytes(Paths.get(preferenceSupplier.scriptGeneratorScriptDefinitionFolders()
							+ scriptDefinitionName + ".py")));
			if (configStoredInJson.equals(actualConfigcontent)) {
				List<Map<String, String>> mappedParamToValue =  currentlyLoadedDataFileContent.getParameterValues();
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
		return gson.toJson(currentlyLoadedDataFileContent);
	}
	
	/**
	 * Get Content from give filename.
	 * @return 
	 * @fileName name of file to get content from
	 * @throws JsonSyntaxException JSON syntax incorrect
	 * @throws JsonIOException trouble reading JSON file
	 * @throws FileNotFoundException JSON file does not exist
	 */
	private ParametersConverter readParametersFromJSONFile(String fileName) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		String filePath = preferenceSupplier.scriptGeneratorDataFileFolder() + "/" + fileName + ".json";
		Gson gson = new Gson();
		ParametersConverter jsonContent = gson.fromJson(new FileReader(filePath), ParametersConverter.class);
		// Check if version number match
		try {
			if (jsonContent.getJSONFormatVersionNumber().equals(CURRENT_VERSION)) {
				return jsonContent;
			} else {
				LOG.error("Json " + jsonContent.getJSONFormatVersionNumber() + " version not supported");
			}
		} catch (NullPointerException e) {
			LOG.error(e + ": Json version number is null");
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
