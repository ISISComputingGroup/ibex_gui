package uk.ac.stfc.isis.ibex.ui.dae.run;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import uk.ac.stfc.isis.ibex.model.Action;

public class ActionButton extends Button {

	public ActionButton(Composite parent, int style, Action action) {
		super(parent, style);
		
		if (action != null) {
			bind(action);
		}
	}

	@Override
	protected void checkSubclass() {
		// Allow sub-classing
	}
	
	private void bind(final Action action) {
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.enabled().observe(this), BeanProperties.value("canExecute").observe(action));
		
		addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				action.execute();
			}
		});
	}
}
