package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MatplotlibFigureViewModel extends PlotUpdateAdapter {
	private final MatplotlibWebsocketModel model;
	private final int figureNumber;
	
	private final SettableUpdatedValue<String> connectionName;

	public MatplotlibFigureViewModel(int figureNumber) {
		this.model = Activator.getModel(figureNumber);
		this.figureNumber = figureNumber;
		model.subscribe(this);
		
		connectionName = new SettableUpdatedValue<String>(
				String.format("[Disconnected] %s Figure %d", model.getServerName(), figureNumber));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnectionStatus(boolean isConnected) {
		if (isConnected) {
			connectionName.setValue(
					String.format("%s Figure %d", model.getServerName(), figureNumber));
		} else {
			connectionName.setValue(
					String.format("[Disconnected] %s Figure %d", model.getServerName(), figureNumber));
		}
	}
	
	/**
	 * Gets the connection name.
	 * @return the connection name
	 */
	public UpdatedValue<String> getConnectionName() {
		return connectionName;
	}
}
