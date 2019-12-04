package uk.ac.stfc.isis.ibex.generation;

import uk.ac.stfc.isis.ibex.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.generation.GeneratorContext;
import uk.ac.stfc.isis.ibex.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.generation.UnsupportedLanguageException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * Class to use to generate a script in a specified language.
 * 
 * @author James King
 *
 */
public class GeneratorFacade {
	
	/**
	 * Part of the strategy design pattern to generate based on which language to use.
	 */
	private static final GeneratorContext generatorContext = new GeneratorContext();	
	
	/**
	 * Generate a python script from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @return The path to the generated script.
	 * @throws InvalidParamsException Thrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported 
	 * 		(this will not happen here as we do support python).
	 */
	public static String generate(ActionsTable actionsTable) throws InvalidParamsException, UnsupportedLanguageException {
		return generate(actionsTable, GeneratedLanguage.PYTHON);
	}
	
	/**
	 * Generate a script in the passed generatedLanguage from the actionsTable passed.
	 * 
	 * @param actionsTable The script generator contents to generate the script from.
	 * @param generatedLanguage The language to generate the script in.
	 * @return The path to the generated script.
	 * @throws InvalidParamsExceptionThrown if the values for the parameters in the actionTable are invalid.
	 * @throws UnsupportedLanguageException Thrown if the language to generate the script in is not supported.
	 */
	public static String generate(ActionsTable actionsTable, GeneratedLanguage generatedLanguage) throws InvalidParamsException, UnsupportedLanguageException {
		return generatorContext.generate(actionsTable, generatedLanguage);
	}
}
