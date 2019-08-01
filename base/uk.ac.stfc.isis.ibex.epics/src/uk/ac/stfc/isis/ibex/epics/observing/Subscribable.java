package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * Interface describing a type which can be subscribed to (e.g. obsevers, writers)
 *
 * <T> - the type of object which is allowed to subscribe.
 */
public interface Subscribable<T> {
	/**
	 * Adds a subscriber to this object.
	 * @param subscriber - the object to subscribe.
	 * @return a subscription.
	 */
    public Subscription subscribe(T subscriber);

    /**
     * Unsubscribe a previously subscribed object.
     * @param subscriber - the object to unsubscribe.
     */
    public void unsubscribe(T subscriber);
}
