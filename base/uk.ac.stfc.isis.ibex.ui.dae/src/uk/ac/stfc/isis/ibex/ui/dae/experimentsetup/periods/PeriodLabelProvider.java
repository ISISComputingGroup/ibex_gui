package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.periods;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.periods.Period;

public class PeriodLabelProvider extends LabelProvider implements ITableLabelProvider {
	
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		Period period = (Period) element;
		switch (columnIndex) {
			case 0: 
				return Integer.toString(period.getNumber());
			case 1:
				return period.getType().toString();
			case 2:
				return Integer.toString(period.getFrames());
			case 3:
				return Integer.toString(period.getBinaryOutput());
			case 4:
				return period.getLabel();
		}
		
		return "";
	}
}
