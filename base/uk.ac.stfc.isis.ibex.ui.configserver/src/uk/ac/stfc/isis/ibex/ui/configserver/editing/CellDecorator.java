package uk.ac.stfc.isis.ibex.ui.configserver.editing;

import org.eclipse.jface.viewers.ViewerCell;

public abstract class CellDecorator<TRow> {
	
	public abstract void applyDecoration(ViewerCell cell);
	
	@SuppressWarnings("unchecked")
	protected TRow getRow(ViewerCell cell) {
		return (TRow) cell.getElement();
	}
}
