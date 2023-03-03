package uk.ac.stfc.isis.ibex.epics.writing;

/**
 * A listener that gets called when a Writable becomes (un)available.
 */
public interface OnCanWriteChangeListener {
    /**
     * Called when the Writable becomes (un)available.
     * @param canWrite Whether the Writable can be written to. 
     */
    void onCanWriteChanged(boolean canWrite);
}
