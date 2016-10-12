package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.scriptgenerator.Row;
import uk.ac.stfc.isis.ibex.scriptgenerator.WaitUnit;

public class SansWaitEditingSupport extends WaitEditingSupport {

	public SansWaitEditingSupport(ColumnViewer viewer) {
		super(viewer);
	}
	
	protected WaitUnit getEnumValueForRow(Row row) {
		return row.getSansWait();
	}

	protected void setEnumForRow(Row row, WaitUnit sansWait) {
		row.setSansWait(sansWait);
	}
	

}
