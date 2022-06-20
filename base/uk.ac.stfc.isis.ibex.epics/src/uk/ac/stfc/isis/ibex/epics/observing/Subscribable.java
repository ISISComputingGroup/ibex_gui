package uk.ac.stfc.isis.ibex.epics.observing;

/**
 * Interface describing a type which can be subscribed to (e.g. observers,
 * writers)
 *
 * @param <T>
 *            - the type of object which is allowed to subscribe.
 */
public interface Subscribable<T> {
	/**
	 * Adds a subscriber to this object.
	 * @param subscriber - the object to subscribe.
	 * @return a subscription.
	 */
    Subscription subscribe(T subscriber);

    /**
     * Unsubscribe a previously subscribed object.
     * @param subscriber - the object to unsubscribe.
     */
    void unsubscribe(T subscriber);
}
