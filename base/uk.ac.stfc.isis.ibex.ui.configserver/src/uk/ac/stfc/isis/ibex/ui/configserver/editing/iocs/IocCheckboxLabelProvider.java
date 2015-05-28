package uk.ac.stfc.isis.ibex.ui.configserver.editing.iocs;

import org.eclipse.core.databinding.observable.map.IObservableMap;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;

import uk.ac.stfc.isis.ibex.ui.configserver.editing.ButtonCellLabelProvider;

public abstract class IocCheckboxLabelProvider<T> extends ButtonCellLabelProvider {
			
	public IocCheckboxLabelProvider(IObservableMap[] stateProperties) {
		super(stateProperties);
	}

	@Override
	public void update(final ViewerCell cell) {
		super.update(cell);

		cell.setText("");
		final T ioc = (T) cell.getElement();		
		final Button checkBox = getButton(cell, SWT.CHECK);
		
		checkBox.setSelection(checked(ioc));	
		checkBox.setText(displayText(ioc));

		checkBox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setChecked(ioc, checkBox.getSelection());
				checkBox.setText(displayText(ioc));
			}
		});
		
		checkBox.setEnabled(isEditable(ioc));
	}
	
	private String displayText(T ioc) {
		return checked(ioc) ? "Yes" : "No";
	}
	
	protected abstract boolean checked(T ioc);
	
	protected abstract void setChecked(T ioc, boolean checked);
	
	protected abstract boolean isEditable(T ioc);
}
