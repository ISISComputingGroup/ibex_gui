package uk.ac.stfc.isis.ibex.model;

/**
 * Has a status to retrieve.
 *
 * @param <T> The type of the retrievable status.
 */
public interface HasStatus<T> {
	
	/**
	 * Retrieve the status of the object.
	 * 
	 * @return the status.
	 */
	T getStatus();

}
