package uk.ac.stfc.isis.ibex.epics.writing;

/**
 * An interface for a listener that gets informed when a Writable goes into error.
 */
public interface OnErrorListener {
    /**
     * Called when the Writable is in error.
     * @param e The error of the Writable.
     */
    void onError(Exception e);
}
