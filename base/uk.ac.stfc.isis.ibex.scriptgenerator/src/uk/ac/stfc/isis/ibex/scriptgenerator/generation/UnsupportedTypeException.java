package uk.ac.stfc.isis.ibex.scriptgenerator.generation;
/**
 * Generic unsupported type exception by ScriptGenerator.
 * Error thrown when ScritpGenerator is asked to generate file(scripts/data files) of unknonwn
 * type.
 * @author mjq34833
 *
 */
@SuppressWarnings("serial")
public class UnsupportedTypeException extends Exception {
	
	public UnsupportedTypeException(String errorMessage) {
		super(errorMessage);
	}

}
