package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Implementing this interface allows classes to listen for changes to a matplotlib plot.
 */
public interface IPlotUpdateListener {
	
	/**
	 * Called when a new plot image is available from the model.
	 */
    public void imageUpdated();
    
    /**
     * Called on change of connection status.
     * @param isConnected true if server is connected
     */
    public void onConnectionStatus(boolean isConnected);
    
    /**
     * Sets the name of this plot.
     * @param newName the new name
     */
    public void onPlotNameChange(String newName);
    
}