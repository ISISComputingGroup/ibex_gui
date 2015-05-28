package uk.ac.stfc.isis.ibex.ui.dae.widgets;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.ui.widgets.GenericEditingSupport;

public abstract class StringEditingSupport<TRow> extends GenericEditingSupport<TRow, String> {

	private ColumnViewer viewer;

	public StringEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, String.class);
		this.viewer = viewer;
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return new TextCellEditor((Composite) viewer.getControl());
	}
	
	@Override
	protected String valueFromString(String text) {
		return text;
	}
}
