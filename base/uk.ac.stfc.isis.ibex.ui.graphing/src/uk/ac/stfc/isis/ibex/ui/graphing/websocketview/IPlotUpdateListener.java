package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

public interface IPlotUpdateListener {
	
    public void imageUpdated();
    
    public void onConnectionStatus(boolean isConnected);
    
}