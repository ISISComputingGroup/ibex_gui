package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeRow;
import uk.ac.stfc.isis.ibex.ui.widgets.DoubleEditingSupport;

public class StepEditingSupport extends DoubleEditingSupport<TimeRegimeRow> {

	public StepEditingSupport(ColumnViewer viewer, Class<TimeRegimeRow> rowType) {
		super(viewer, rowType);
	}

	@Override
	protected Double valueFromRow(TimeRegimeRow row) {
		return row.getStep();

	}

	@Override
	protected void setValueForRow(TimeRegimeRow row, Double value) {
		row.setStep(value);	
	}
}
