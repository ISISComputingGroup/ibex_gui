package uk.ac.stfc.isis.ibex.ui.widgets;

import org.csstudio.opibuilder.visualparts.DoubleCellEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;

public abstract class DoubleEditingSupport<TRow> extends GenericEditingSupport<TRow, Double> {

	private CellEditor editor;
	
	public DoubleEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, Double.class);
		editor = new DoubleCellEditor((Composite) viewer.getControl());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}
	
	@Override
	protected Double valueFromString(String text) {
		try {
			return Double.parseDouble(text);
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
}
