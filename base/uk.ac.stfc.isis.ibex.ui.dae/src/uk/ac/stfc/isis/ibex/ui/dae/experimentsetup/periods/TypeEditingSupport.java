package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import org.eclipse.jface.viewers.ColumnViewer;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;
import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.PeriodType;
import uk.ac.stfc.isis.ibex.ui.widgets.EnumEditingSupport;

public class TypeEditingSupport extends EnumEditingSupport<Period, PeriodType> {

	public TypeEditingSupport(ColumnViewer viewer, Class<Period> rowType, Class<PeriodType> enumType) {
		super(viewer, rowType, enumType);
	}

	@Override
	protected PeriodType getEnumValueForRow(Period row) {
		return row.getType();
	}

	@Override
	protected void setEnumForRow(Period row, PeriodType value) {
		row.setType(value);
	}

}
