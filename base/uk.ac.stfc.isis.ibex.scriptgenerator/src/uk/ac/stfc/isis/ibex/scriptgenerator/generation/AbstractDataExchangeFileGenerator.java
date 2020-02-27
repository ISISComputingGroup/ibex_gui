package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.io.FileNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;


/**
 * Abstract class that needs to be extended for classes that are responsible for saving parameter values to a given file format.
 * @author mjq34833
 *
 */
public abstract class AbstractDataExchangeFileGenerator extends ModelObject implements Strategy  {
	
	/**
	 * Following operation only supported in AbstractProgrammingLanguageGenerator
	 */
	@Override
	public void generate(List<ScriptGeneratorAction> scriptGenContent, ParametersConverter currentlyLoadedDataFile, Config config) throws InterruptedException, ExecutionException {
		throw new UnsupportedOperationException();
		
	}
    /**
     * Gets list of available files in DataFile directories
     * @throws FileNotFoundException when folder is not present
     */
    public abstract void getListOfAvailableDataFiles() throws FileNotFoundException;
    /**
     * Get Content of given filename.
     * @param filename File to read the content from
     * @throws FileNotFoundException file not found
     * @throws JsonIOException error when reading file
     * @throws JsonSyntaxException not valid JSON syntax
     */
    public abstract void getContent(String filename) throws JsonSyntaxException, JsonIOException, FileNotFoundException;
    
    /**
     * Abstract generator method for generating data exchange file
     * @param scriptGenContent content to generate from
     * configName configuration to use for generation 
     */
    public abstract void generate(List<ScriptGeneratorAction> scriptGenContent, String configName) throws InterruptedException, ExecutionException;
    
    /**
     * get current time
     * @return current date
     */
    protected String getCurrentDate() {
    	DateFormat dateFormat = new SimpleDateFormat("yyy/MM/dd");
    	Date date = new Date();
    	return dateFormat.format(date);
    }
    
    /**
     * get current time
     * @return current time
     */
    protected String getCurrentTime() {
    	DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    	Date date = new Date();
    	return dateFormat.format(date);
    }

}
