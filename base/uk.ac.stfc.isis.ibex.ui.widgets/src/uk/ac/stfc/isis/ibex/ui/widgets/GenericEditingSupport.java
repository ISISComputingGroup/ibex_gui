package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

public abstract class GenericEditingSupport<TRow, T> extends EditingSupport {

	private final ColumnViewer viewer;
	private final Class<TRow> rowType;
	private final Class<T> valueType;
	
	public GenericEditingSupport(ColumnViewer viewer, Class<TRow> rowType, Class<T> valueType) {
		super(viewer);
		this.viewer = viewer;
		this.rowType = rowType;
		this.valueType = valueType;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		if (!rowType.isInstance(element)) {
			return null;
		}
		
		TRow row = rowType.cast(element);
		return valueFromRow(row);		
	}

	@Override
	protected void setValue(Object element, Object value) {
		if (!rowType.isInstance(element)) {
			return;
		}
		
		TRow row = rowType.cast(element);
		T newValue = null;
		if (valueType.isInstance(value)) {
			newValue = valueType.cast(value);
		} else if (value instanceof String) {
			newValue = valueFromString((String) value);
		} else {
			return;
		}		
		
		if (newValue == null) {
			return;
		}
		
		T rowValue = valueFromRow(row);
		if (rowValue == null || rowValue.equals(newValue)) {
			return;
		}
		
		setValueForRow(row, newValue);
		viewer.update(element, null);
	}
	
	protected abstract T valueFromString(String value);

	protected abstract T valueFromRow(TRow row);

	protected abstract void setValueForRow(TRow row, T value);

}
