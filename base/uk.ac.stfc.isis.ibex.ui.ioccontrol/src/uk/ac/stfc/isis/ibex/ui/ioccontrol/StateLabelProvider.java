package uk.ac.stfc.isis.ibex.ui.ioccontrol;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.configserver.EditableIocState;

public class StateLabelProvider extends ObservableMapCellLabelProvider {

	public static final String TEXT_RUNNING = "Running";
	public static final String TEXT_NOT_RUNNING = "Stopped";
	
	public static final Color COLOR_RUNNING = SWTResourceManager.getColor(19, 145, 44); // Green
	public static final Color COLOR_STOPPED = SWTResourceManager.getColor(173, 66, 66); // RED

	public StateLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}

	@Override
	public void update(ViewerCell cell) {
		super.update(cell);

		EditableIocState state = (EditableIocState) cell.getElement();
		boolean isRunning = state != null && state.getIsRunning();
		cell.setText(isRunning ? TEXT_RUNNING : TEXT_NOT_RUNNING);
		cell.setForeground(isRunning ? COLOR_RUNNING : COLOR_STOPPED);
	}
}
