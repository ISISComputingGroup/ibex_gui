package uk.ac.stfc.isis.ibex.scriptgenerator.generation;

/**
 * The language to generate a script in (currently only Python is supported).
 * 
 * @author James King
 *
 */
public enum GeneratedLanguage {
	
	PYTHON {
		public String toString() {
			return "Python";
		}
	},
	
	UNSUPPORTED_LANGUAGE {
		public String toString() {
			return "Unsupported";
		}
	}
	
}
