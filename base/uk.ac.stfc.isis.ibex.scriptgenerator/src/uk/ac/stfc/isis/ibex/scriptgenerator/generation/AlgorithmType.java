package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * Type of Algorithm to use for different strategies
 * @author mjq34833
 *
 */

public enum AlgorithmType {
	GENERATE_PYTHON("generate python"),
	GENERATE_JSON("generate json"),
	PARAMETERS_VALIDITY_CHECK("parameters validity check"),
	VALIDITY_ERROR_CHECK("validity error check"),
	LOAD_FILENAMES("load file names"),
	GET_CONTENT("get content");
	
	/**
	 * name of the algorithm
	 */
	private String name;

	private AlgorithmType(String name) {
		this.name = name;
	}
	
	/**
	 * Get name of current algorithm.
	 * @return name of the algorithm
	 */
	public String getName() {
		return this.name;
	}
}
