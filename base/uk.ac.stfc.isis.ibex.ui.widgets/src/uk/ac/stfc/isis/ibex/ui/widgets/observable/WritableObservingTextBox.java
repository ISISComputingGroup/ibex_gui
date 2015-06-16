package uk.ac.stfc.isis.ibex.ui.widgets.observable;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.wb.swt.SWTResourceManager;

/**
 * A text box for displaying and editing a record
 *
 */
public class WritableObservingTextBox extends Composite {
	
	private DataBindingContext bindingContext;
	
	private final Text textbox;
	private final Button setButton;
	
	public WritableObservingTextBox(
			Composite parent, 
			int style, 
			WritableObservableAdapter adapter) {
		super(parent, style);
		int numCols = (style & SWT.UP) != 0 ? 1 : 2;
		GridLayout gridLayout = new GridLayout(numCols, false);
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		textbox = new Text(this, SWT.BORDER);
		textbox.setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		textbox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));	
				
		setButton = new Button(this, SWT.NONE);
		setButton.setText("Update");
		
		if (adapter != null) {
			bind(adapter);
		}
	}

	private void bind(final WritableObservableAdapter adapter) {
		bindingContext = new DataBindingContext();	
		bindingContext.bindValue(WidgetProperties.enabled().observe(setButton), BeanProperties.value("value").observe(adapter.canSetText()));
		bindingContext.bindValue(WidgetProperties.enabled().observe(textbox), BeanProperties.value("value").observe(adapter.canSetText()));
		bindingContext.bindValue(WidgetProperties.text().observe(textbox), BeanProperties.value("value").observe(adapter.text()));
		
		textbox.addListener(SWT.Traverse, new Listener() {
	        @Override
	        public void handleEvent(Event event) {
	            if (event.detail == SWT.TRAVERSE_RETURN) {
					setText(adapter);
	            }
	        }
	    });
		
		setButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);
				setText(adapter);
			}
		});
	}
	
	private void setText(WritableObservableAdapter adapter) {
		adapter.setText(textbox.getText());
	}
}
