package uk.ac.stfc.isis.ibex.ui.widgets;

import org.csstudio.opibuilder.visualparts.IntegerCellEditor;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.swt.widgets.Composite;

public abstract class IntegerEditingSupport<TRow> extends GenericEditingSupport<TRow, Integer> {

	private CellEditor editor;
	
	public IntegerEditingSupport(ColumnViewer viewer, Class<TRow> rowType) {
		super(viewer, rowType, Integer.class);
		editor = new IntegerCellEditor((Composite) viewer.getControl());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {	
		return editor;
	}
	
	@Override
	protected Integer valueFromString(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
		
	}
}
