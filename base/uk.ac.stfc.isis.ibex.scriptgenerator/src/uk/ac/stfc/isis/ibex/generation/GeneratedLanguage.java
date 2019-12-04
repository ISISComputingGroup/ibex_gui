package uk.ac.stfc.isis.ibex.generation;

/**
 * The language to generate a script in (currently only Python)
 * 
 * @author James King
 *
 */
public enum GeneratedLanguage {
	
	PYTHON{
		public String toString() {
			return "Python";
		}
	}
	
}
