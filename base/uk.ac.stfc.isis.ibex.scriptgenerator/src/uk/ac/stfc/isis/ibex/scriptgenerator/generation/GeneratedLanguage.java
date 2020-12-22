
package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * The language to generate a script in (currently only Python is supported by being added to the GeneratorContext),
 *  Unsupported language is for testing only!).
 * 
 *
 */
public enum GeneratedLanguage {
	
	/** Python is the language to generate. */
	PYTHON("Python"), 
	/** Unsupported language is to be used only for testing. */
	UNSUPPORTED_LANGUAGE("Unsupported");
	
	private final String name;
	
	/**
	 * Constructor.
	 * @param name name of the language
	 */
	GeneratedLanguage(String name) {
		this.name = name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return name;
	}
	
}