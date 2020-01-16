package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * The language to generate a script in (currently only Python is supported by being added to the GeneratorContext),
 *  Unsupported language is for testing only!).
 * 
 * @author James King
 *
 */
public enum GeneratedLanguage {
	
	PYTHON("Python"), UNSUPPORTED_LANGUAGE("Unsupported"); // Unsupported language is to be used only for testing
	
	private final String name;
	
	private GeneratedLanguage(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
