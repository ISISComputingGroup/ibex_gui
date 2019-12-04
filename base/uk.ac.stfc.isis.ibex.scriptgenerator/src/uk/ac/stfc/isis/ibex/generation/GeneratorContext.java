package uk.ac.stfc.isis.ibex.generation;

import java.util.HashMap;
import java.util.Map;

import uk.ac.stfc.isis.ibex.generation.GeneratedLanguage;
import uk.ac.stfc.isis.ibex.generation.GeneratorInterface;
import uk.ac.stfc.isis.ibex.generation.GeneratorPython;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;
import uk.ac.stfc.isis.ibex.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.generation.UnsupportedLanguageException;

public class GeneratorContext {
	
	/**
	 * A mapping from the type of generated language to it's generator.
	 */
	private Map<GeneratedLanguage, GeneratorInterface> generatorStrategies = new HashMap<>();
	
	/**
	 * Add each languages generator to the strategies.
	 */
	public GeneratorContext() {
		generatorStrategies.put(GeneratedLanguage.PYTHON, new GeneratorPython());
	}
	
	public String generate(ActionsTable actionsTable, GeneratedLanguage generatedLanguage) throws InvalidParamsException, UnsupportedLanguageException {
		if (generatorStrategies.containsKey(generatedLanguage)) {
			return generatorStrategies.get(generatedLanguage).generate(actionsTable);
		} else {
			throw new UnsupportedLanguageException("Language " + generatedLanguage + " not supported");
		}
	}

}
