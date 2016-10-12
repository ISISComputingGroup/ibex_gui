package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.scriptgenerator.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.WaitUnit;

public class TransWaitEditingSupport extends WaitEditingSupport {

	public TransWaitEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}

	protected WaitUnit getEnumValueForRow(Row row) {
		return row.getTransWait();
	}

	protected void setEnumForRow(Row row, WaitUnit transWait) {
		row.setTransWait(transWait);
	}

}
