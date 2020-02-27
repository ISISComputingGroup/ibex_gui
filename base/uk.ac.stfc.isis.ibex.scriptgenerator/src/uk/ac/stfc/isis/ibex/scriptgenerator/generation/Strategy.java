package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.util.List;
import java.util.concurrent.ExecutionException;

import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ScriptGeneratorAction;

/**
 * Interface class inherited by AbstractProgrammingLanugageGenerator and AbstractDataExchangeFileGenerator.
 * This class provides with run time polymorphism in GeneratorContext to run different generate method depending
 * on what is generated for e.g. Python, JSON files etc.
 * @author mjq34833
 *
 */
public interface Strategy {
	
	/**
	 * Refresh the generated script property with a script (String).
	 * 
	 * @param scriptGenContent The script generator content to produce the script from.
	 * @param config The instrument config to generate the script with.
	 * @throws ExecutionException A failure to execute the call to generate a script
	 * @throws InterruptedException The call to generate a script was interrupted
	 */
	 void generate(List<ScriptGeneratorAction> scriptGenContent, ParametersConverter currentlyLoadedDataFile,
			 Config config) throws InterruptedException, ExecutionException;
	 /**
	  * two types of strategy: programming language and data exchange format
	  * @return
	  */
	 void generate(List<ScriptGeneratorAction> scriptGenContent, String configName)
			 throws InterruptedException, ExecutionException;
	
	 

}
