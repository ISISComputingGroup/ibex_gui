package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

public abstract class StringEditingSupport<TRow> extends GenericEditingSupport<TRow, String> {

	private final TextCellEditor editor;

	public StringEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, String.class);
		editor = new TextCellEditor((Composite) viewer.getControl());
	}

	@Override
	protected String valueFromString(String value) {
		return value;
	}

	@Override
	protected CellEditor getCellEditor(Object arg0) {
		return editor;
	}
}
