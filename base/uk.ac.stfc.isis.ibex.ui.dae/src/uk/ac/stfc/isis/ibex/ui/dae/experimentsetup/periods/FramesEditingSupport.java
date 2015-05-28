package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.ui.widgets.IntegerEditingSupport;

public class FramesEditingSupport extends IntegerEditingSupport<Period> {

	public FramesEditingSupport(ColumnViewer viewer, Class<Period> rowType) {
		super(viewer, rowType);
	}

	@Override
	protected Integer valueFromRow(Period row) {
		return row.getFrames();
	}

	@Override
	protected void setValueForRow(Period row, Integer value) {
		row.setFrames(value);
	}
}
