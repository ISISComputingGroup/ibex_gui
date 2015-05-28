package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeMode;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeRow;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

public class TimeRegimeModeEditingSupport extends EnumEditingSupport<TimeRegimeRow, TimeRegimeMode> {

	public TimeRegimeModeEditingSupport(ColumnViewer viewer, Class<TimeRegimeRow> rowType, Class<TimeRegimeMode> enumType) {
		super(viewer, rowType, enumType);
	}

	@Override
	protected TimeRegimeMode getEnumValueForRow(TimeRegimeRow row) {
		return row.getMode();
	}

	@Override
	protected void setEnumForRow(TimeRegimeRow row, TimeRegimeMode value) {
		row.setMode(value);
	}
}
