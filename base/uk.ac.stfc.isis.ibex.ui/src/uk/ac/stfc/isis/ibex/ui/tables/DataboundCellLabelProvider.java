package uk.ac.stfc.isis.ibex.ui.tables;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.databinding.viewers.ObservableMapCellLabelProvider;
import org.eclipse.jface.viewers.ViewerCell;

public abstract class DataboundCellLabelProvider<TRow> extends ObservableMapCellLabelProvider {

	public DataboundCellLabelProvider(IObservableMap attributeMap) {
		super(attributeMap);
	}

	@Override
	public void update(ViewerCell cell) {
		TRow row = getRow(cell);
		cell.setText(valueFromRow(row));
	}

	@SuppressWarnings("unchecked")
	protected TRow getRow(ViewerCell cell) {
		return (TRow) cell.getElement();
	}

	protected abstract String valueFromRow(TRow row);
	
	protected String valueOrEmpty(Double value) {
		return value == null ? "" : Double.toString(value);
	}
}
