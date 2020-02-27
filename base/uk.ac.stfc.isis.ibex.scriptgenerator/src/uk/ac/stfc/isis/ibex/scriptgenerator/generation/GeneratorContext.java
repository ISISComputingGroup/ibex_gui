package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import uk.ac.stfc.isis.ibex.scriptgenerator.generation.GeneratedProgrammingLanguage;
import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.ModelObject;
import uk.ac.stfc.isis.ibex.scriptgenerator.ScriptGeneratorSingleton;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.AbstractProgrammingLanguageGenerator;
import uk.ac.stfc.isis.ibex.scriptgenerator.generation.UnsupportedTypeException;
import uk.ac.stfc.isis.ibex.scriptgenerator.pythoninterface.Config;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Part of the strategy pattern to generate a script in a specified language,
 * specifically used by GeneratorFacade to pass a language to and use the correct generator from that language.
 * 
 * @author James King
 *
 */
public class GeneratorContext extends ModelObject {
	private static final Logger LOG = IsisLog.getLogger(ScriptGeneratorSingleton.class);
	
	/**
	 * A mapping from the type of generated language to it's generator.
	 */
	private Map<GenerationType, Strategy> generatorStrategies = new HashMap<>();
	
	
	/**
	 * Add listener to the generators present in this context
	 * @param propertyName property to listen to
	 * @param type is it a programming language generator or data exchange format file generator?
	 */
	public void AddListenerToGenerator(String propertyName, GenerationType type) {
		try {
			if (type.getType() == GenerationType.PROGRAMMING_LANGUAGE) {
				AbstractProgrammingLanguageGenerator generator = (AbstractProgrammingLanguageGenerator)getGenerator(type);
				generator.addPropertyChangeListener(propertyName, evt -> {
					firePropertyChange(propertyName, evt.getOldValue(), evt.getNewValue());
					});
				
			} else if (type.getType() == GenerationType.DATA_EXCHANGE_FORMAT) {
				AbstractDataExchangeFileGenerator generator = (AbstractDataExchangeFileGenerator)getGenerator(type);
				generator.addPropertyChangeListener(propertyName, evt -> {
					firePropertyChange(propertyName, evt.getOldValue(), evt.getNewValue());
					});
			}
		} catch (UnsupportedTypeException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
			e.printStackTrace();
		}
		
	}

	
	/**
	 * Get a generator for the specified generator language. Throws exception if not supported
	 * 
	 * @param generatedLanguage The language to generate or check validity with.
	 * @return The generator to carry out the generation/check validity with.
	 * @throws UnsupportedTypeException If the language has no supported generator throw this.
	 */
	private Strategy getGenerator(GenerationType generatedLanguage) throws UnsupportedTypeException {
		return Optional.ofNullable(generatorStrategies.get(generatedLanguage))
				.orElseThrow(() -> new UnsupportedTypeException(generatedLanguage.getType() + " " + generatedLanguage + " not supported"));
	}
	
	/**
	 * Add each language's generator to the strategies.
	 * 
	 * @param pythonInterface The python interface to set up the python generator with.
	 */
	public GeneratorContext(GeneratedProgrammingLanguage language, Strategy strategy) {
		generatorStrategies.put(language, strategy);
	}
	
	/**
	 * Run strategy for generating script, performing validity check and validity error check
	 * @param algorithm types of algorithm to run the strategy with
	 * @param actionsTable The script generator contents to generate the script from
	 * @param config the instrument config to generate the script from
	 * @param langauge type of generator use, each language has their own generator 
	 * @throws UnsupportedTypeException
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	public void doRunstrategy(AlgorithmType algorithm, ParametersConverter currentlyLoadedDataFile, ActionsTable actionsTable, Config config, GeneratedProgrammingLanguage langauge)
			throws UnsupportedTypeException, InterruptedException, ExecutionException {
		AbstractProgrammingLanguageGenerator generator = (AbstractProgrammingLanguageGenerator)getGenerator(langauge);
		switch(algorithm) {
		  case GENERATE_PYTHON:
			generator.generate(actionsTable.getActions(), currentlyLoadedDataFile, config);
			break;
		  case PARAMETERS_VALIDITY_CHECK:
			generator.refreshAreParamsValid(actionsTable.getActions(), config);
			break;
		  case VALIDITY_ERROR_CHECK:
			generator.refreshValidityErrors(actionsTable.getActions(), config);
			break;
		default:
			LOG.error(" %s does not exist for %s", algorithm.getName(), langauge.toString());
			break;
			
		}
	
	}
	
	/**
	 * Run Strategy for Generating Data Exchange File format and loading filenames
	 * @param algorithm type of algorithm to use
	 * @param actionsTable content to save 
	 * @param fileFormat type of generator to use
	 * @throws UnsupportedTypeException unsupported generator type
	 * @throws FileNotFoundException when folder where data file is saved is not available
	 */
	public void doRunstrategy(AlgorithmType algorithm, ActionsTable actionsTable, GeneratedDataExchangeFormat format, String configName)
	throws InterruptedException, ExecutionException, UnsupportedTypeException, FileNotFoundException {
		
		AbstractDataExchangeFileGenerator generator = (AbstractDataExchangeFileGenerator)getGenerator(format);
		switch(algorithm) {
		case GENERATE_JSON:
			generator.generate(actionsTable.getActions(), configName);
			break;
		case LOAD_FILENAMES:
			generator.getListOfAvailableDataFiles();
			break;
		default:
			LOG.error("%s does not exist for %s", algorithm.getName(), format.toString());
			break;
		
		}
	}

	/**
	 * Run Strategy for Reading content of Data Exchange file
	 * @param algorith type of algorithm to run
	 * @param filename to read the content from
	 * @param format type of generate
	 * @throws FileNotFoundException 
	 * @throws JsonIOException 
	 * @throws JsonSyntaxException 
	 */
	public void doRunstrategy(AlgorithmType algorithm, String filename, GeneratedDataExchangeFormat format) 
			throws JsonSyntaxException, JsonIOException, FileNotFoundException {
		try {
			if (algorithm == AlgorithmType.GET_CONTENT) {
				AbstractDataExchangeFileGenerator generator = (AbstractDataExchangeFileGenerator)getGenerator(format);
				generator.getContent(filename);
			} else {
				LOG.error("%s does not exist for %s", algorithm.getName(), format.toString());
			}
		} catch (UnsupportedTypeException e) {
			// TODO Auto-generated catch block
			LOG.error(e);
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Add generator
	 * @param type that corresponds to type of generator to use i.e. PROGRAMMING LANGUAGE or DATA EXCHANGE FORMAT
	 * @param generator type of generator to add
	 */
	public void addGenerator(GenerationType type, Strategy generator) {
		generatorStrategies.put(type, generator);
	}

}
