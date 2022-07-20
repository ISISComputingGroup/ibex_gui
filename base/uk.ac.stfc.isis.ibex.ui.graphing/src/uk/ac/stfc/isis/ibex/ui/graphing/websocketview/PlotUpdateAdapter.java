package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

/**
 * Adapter for the IPlotUpdateListener interface.
 */
public class PlotUpdateAdapter implements IPlotUpdateListener {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void imageUpdated() {}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnectionStatus(boolean isConnected) {}
    
}