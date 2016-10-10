package uk.ac.stfc.isis.ibex.ui.widgets;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;

public class ComboCellLabelProvider extends ControlCellLabelProvider<Combo> {
	
	protected ComboCellLabelProvider(IObservableMap[] attributeMaps) {
		super(attributeMaps);
	}

	protected Combo createControl(final ViewerCell cell, int style) {
		Combo combo = new Combo(composite(cell), style);
		combo.setText("");
		combo.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		return combo;
	}
}
