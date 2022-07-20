package uk.ac.stfc.isis.ibex.ui.graphing.websocketview;

import java.io.Closeable;
import org.apache.logging.log4j.Logger;

import uk.ac.stfc.isis.ibex.logger.IsisLog;
import uk.ac.stfc.isis.ibex.model.SettableUpdatedValue;
import uk.ac.stfc.isis.ibex.model.UpdatedValue;

public class MatplotlibFigureViewModel extends PlotUpdateAdapter implements Closeable {
	
	private static final Logger LOG = IsisLog.getLogger(MatplotlibRendererViewModel.class);
	
	private final MatplotlibWebsocketModel model;
	
	private final SettableUpdatedValue<String> plotName;

	public MatplotlibFigureViewModel(int figureNumber) {
		this.model = new MatplotlibWebsocketModel("127.0.0.1", 8988, figureNumber);
		model.subscribe(this);
		
		plotName = new SettableUpdatedValue<String>(
				String.format("[Disconnected] %s", model.getPlotName(), figureNumber));
	}
	
	public MatplotlibWebsocketModel getModel() {
		return model;
	}
	
	private void updatePlotName() {
		if (model.isConnected()) {
			plotName.setValue(model.getPlotName());
		} else {
			plotName.setValue(
					String.format("[Disconnected] %s", model.getPlotName()));
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onConnectionStatus(boolean isConnected) {
		updatePlotName();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void onPlotNameChange(String plotName) {
		updatePlotName();
	}
	
	/**
	 * Gets the connection name.
	 * @return the connection name
	 */
	public UpdatedValue<String> getPlotName() {
		return plotName;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void close() {
		model.unsubscribe(this);
		model.close();
	}
}
