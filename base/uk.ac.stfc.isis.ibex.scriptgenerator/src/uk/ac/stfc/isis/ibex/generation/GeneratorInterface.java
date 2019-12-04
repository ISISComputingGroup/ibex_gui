package uk.ac.stfc.isis.ibex.generation;

import uk.ac.stfc.isis.ibex.generation.InvalidParamsException;
import uk.ac.stfc.isis.ibex.scriptgenerator.table.ActionsTable;

/**
 * An interface to be implemented by each different language for the script generator.
 * 
 * @author James King
 *
 */
public interface GeneratorInterface {
	
	public String generate(ActionsTable actionsTable) throws InvalidParamsException;
	
}
