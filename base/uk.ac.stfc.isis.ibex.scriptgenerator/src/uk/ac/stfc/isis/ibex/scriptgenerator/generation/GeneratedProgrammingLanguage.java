package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * The language to generate a script in (currently only Python is supported by being added to the GeneratorContext),
 *  Unsupported language is for testing only!).
 * 
 * @author James King
 *
 */
public enum GeneratedProgrammingLanguage implements GenerationType {
	
	PYTHON("Python"), UNSUPPORTED_LANGUAGE("Unsupported"); // Unsupported language is to be used only for testing
	
	private final String name;
	
	private GeneratedProgrammingLanguage(String name) {
		this.name = name;
	}
	
	/**
	 * Get string representation of enum 
	 * @return name of enum in string
	 */
	public String toString() {
		return name;
	}
	
	@Override
	public String getType() {
		return GenerationType.PROGRAMMING_LANGUAGE;
	}
	
}
