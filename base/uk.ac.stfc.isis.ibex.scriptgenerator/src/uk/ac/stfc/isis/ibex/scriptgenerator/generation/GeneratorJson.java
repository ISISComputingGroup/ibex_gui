package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import org.apache.logging.log4j.Logger;
import org.eclipse.core.runtime.Platform;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.preferences.PreferenceSupplier;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.ScriptDefinitionWrapper;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Strategy for handling JSON related operations i.e. Saving data files in JSON format, 
 * reading content from user selected JSON file and listing the JSON files available in
 * DataFile folder.
 * @author mjq34833
 *
 */
public class GeneratorJson extends AbstractDataExchangeFileGenerator {
	/** 
	 * Currently supported JSON file version by us
	 */
	private String CURRENT_VERSION = "1";
	private final PreferenceSupplier preferenceSupplier = new PreferenceSupplier();
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	
	/**
	 * Generate JSON string from the parameters and add meta data.
	 * @param scriptGenContent content to generate JSON string from
	 * @param configContent actual content of configuration file
	 * @param configName name of the configuration used to generate JSON
	 */
	private void generateJson(List<Map<String, String>> scriptGenContent, String configContent, String configName) {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String version = Platform.getProduct().getDefiningBundle().getVersion().toString();
		String configFilePath = preferenceSupplier.scriptGeneratorScriptDefinitionFolders() + configName + ".py";
		String json = gson.toJson(new ParametersConverter(CURRENT_VERSION, scriptGenContent, configContent, "",
				getCurrentDate(), getCurrentTime(), configFilePath, "", version));
		
		firePropertyChange("Values saved", null, Optional.ofNullable(json));
	}


	/**
	 * Get list of data files from the directory where data files are saved.
	 * @return list of files 
	 * @throws FileNotFoundException when DataFiles folder does not exist
	 */
	private List<String> lsitOfdataFiles() throws FileNotFoundException {
		List<String> list = new ArrayList<String>();
		
		if (Files.isDirectory(Paths.get(preferenceSupplier.dataFileFolder()))) {
			File folder = new File(preferenceSupplier.dataFileFolder());
			list = Arrays.asList(folder.list((dir, name) ->(name.endsWith(".json"))));
			// remove extension
			for (ListIterator<String> itr = list.listIterator(); itr.hasNext();) {
				list.set(itr.nextIndex(), itr.next().replace(".json", ""));
			}
			
		} else {
			throw new FileNotFoundException("C:/DataFile not found");
		}
		return list;
	}
	
	/**
	 * FirePropertyChange to notify GeneratorContext of available data files.
	 */
	public void getListOfAvailableDataFiles() throws FileNotFoundException{
	
		firePropertyChange("data files", null, lsitOfdataFiles());
		
	}

	/**
	 * Wrapper function for generating a generator specific content which then is saved.
	 * @param scriptGenContent content to generate data file with
	 * @param configName name of the configuration used to generate data file
	 */
	@Override
	public void generate(List<ScriptGeneratorAction> scriptGenContent, String configName) throws InterruptedException, ExecutionException {
		try {
			String content = new String(Files.readAllBytes(Paths.get(preferenceSupplier.scriptGeneratorScriptDefinitionFolders() + configName + ".py")));
			generateJson(scriptGenContent.stream()
					.map(action -> action.getAllActionParametersAsString()).collect(Collectors.toList()), content.toString(), configName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	/**
	 * Get Content from give filename
	 * @fileName name of file to get content from
	 * @throws JsonSyntaxException JSON syntax incorrect
	 * @throws JsonIOException trouble reading JSON file
	 * @throws FileNotFoundException JSON file does not exist
	 */
	@Override
	public void getContent(String fileName) throws JsonSyntaxException, JsonIOException, FileNotFoundException{
		String filePath = preferenceSupplier.dataFileFolder() + "/" + fileName + ".json";
		Gson gson = new Gson();
		ParametersConverter jsonContent = gson.fromJson(new FileReader(filePath), ParametersConverter.class);
		// Check if version number match
		try {
			if (jsonContent.getVersionNumber().equals(CURRENT_VERSION)) {
				firePropertyChange("file content", null, jsonContent);
			} else {
				firePropertyChange("unsupported version", null, new ArrayList<String>(Arrays.asList(jsonContent.getVersionNumber(), CURRENT_VERSION)));
			}
		} catch (NullPointerException e) {
			LOG.error(e + ": Json version number is null");
			firePropertyChange("unsupported version", null, new ArrayList<String>(Arrays.asList("not supported", CURRENT_VERSION)));
		}
	}


	@Override
	public void generate(List<ScriptGeneratorAction> scriptGenContent, ParametersConverter currentlyLoadedDataFile,
			ScriptDefinitionWrapper scriptDefinitionWrapper) throws InterruptedException, ExecutionException {
		// TODO Auto-generated method stub
		
	}
	
}
