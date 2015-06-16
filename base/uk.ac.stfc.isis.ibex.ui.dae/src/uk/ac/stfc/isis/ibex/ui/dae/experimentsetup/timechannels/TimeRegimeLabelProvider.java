package uk.ac.stfc.isis.ibex.ui.dae.experimentsetup.timechannels;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import uk.ac.stfc.isis.ibex.dae.experimentsetup.timechannels.TimeRegimeRow;

public class TimeRegimeLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		TimeRegimeRow row = (TimeRegimeRow) element;
		switch (columnIndex) {
			case 0: 
				return Double.toString(row.getFrom());
			case 1:
				return Double.toString(row.getTo());
			case 2:
				return Double.toString(row.getStep());
			case 3:
				return row.getMode().toString();
		}
		
		return "";
	}
}
