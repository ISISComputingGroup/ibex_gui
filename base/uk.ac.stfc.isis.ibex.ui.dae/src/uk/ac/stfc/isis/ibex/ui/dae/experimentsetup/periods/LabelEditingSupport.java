package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.ui.dae.widgets.StringEditingSupport;

public class LabelEditingSupport extends StringEditingSupport<Period> {

	public LabelEditingSupport(ColumnViewer viewer, Class<Period> rowType) {
		super(viewer, rowType);
	}

	@Override
	protected String valueFromRow(Period row) {
		return row.getLabel();
	}

	@Override
	protected void setValueForRow(Period row, String value) {
		row.setLabel(value);
	}

}
