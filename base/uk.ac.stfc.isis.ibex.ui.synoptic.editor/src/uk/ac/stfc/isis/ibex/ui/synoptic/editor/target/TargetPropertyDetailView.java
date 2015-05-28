package uk.ac.stfc.isis.ibex.ui.synoptic.editor.target;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Text;

import uk.ac.stfc.isis.ibex.synoptic.model.desc.Property;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.IPropertySelectionListener;
import uk.ac.stfc.isis.ibex.ui.synoptic.editor.model.InstrumentViewModel;

public class TargetPropertyDetailView extends Composite {
	
	private Label propertyLabel;
	private Composite propertyComposite;
	private Text key;
	private Text value;
	private InstrumentViewModel model;

	private boolean updateLock;
	
	private final IPropertySelectionListener propertyListener = new IPropertySelectionListener() {		
		@Override
		public void selectionChanged(Property oldProperty, Property newProperty) {
			setProperty(newProperty);
		}
	};
	
	private final Listener propertyUpdateListener = new Listener() {
		@Override
		public void handleEvent(Event event) {
			if (!updateLock && model != null) {
				model.updateSelectedProperty(new Property(key.getText(), value.getText()));
			}			
		}
	};
		
	public TargetPropertyDetailView(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		propertyLabel = new Label(this, SWT.NONE);
		propertyLabel.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		propertyLabel.setText("Select a property to view/edit details");
		
		propertyComposite = new Composite(this, SWT.NONE);
		propertyComposite.setLayout(new GridLayout(2, false));
		propertyComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		
		Label lblKey = new Label(propertyComposite, SWT.NONE);
		lblKey.setAlignment(SWT.RIGHT);
		lblKey.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblKey.setText("Key:");
		
		key = new Text(propertyComposite, SWT.BORDER);
		key.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		key.addListener(SWT.FocusOut, propertyUpdateListener);
		
		Label lblValue = new Label(propertyComposite, SWT.NONE);
		lblValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblValue.setText("Value:");
		lblValue.setAlignment(SWT.RIGHT);
		
		value = new Text(propertyComposite, SWT.BORDER);
		value.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		value.addListener(SWT.FocusOut, propertyUpdateListener);
	}
	
	public void setModel(InstrumentViewModel model) {
		if (this.model != null) {
			model.removePropertySelectionListener(propertyListener);
		}
		
		if (model != null) {
			model.addPropertySelectionListener(propertyListener);
			setProperty(model.getSelectedProperty());		
		} else {
			setProperty(null);
		}
		
		this.model = model;
	}
	
	private void setProperty(Property property) {	
		updateLock = true;
		
		boolean validProperty = property != null;
		showPropertyLabel(!validProperty);
		showPropertyComposite(validProperty);
		if (validProperty) {
			key.setText(property.key());
			value.setText(property.value());
		}		
		
		updateLock = false;
	}

	private void showPropertyComposite(boolean show) {
		propertyComposite.setVisible(show);
		GridData gd = (GridData) propertyComposite.getLayoutData();
		gd.exclude = !show;
		layout();
	}

	private void showPropertyLabel(boolean show) {
		propertyLabel.setVisible(show);
		GridData gd = (GridData) propertyLabel.getLayoutData();
		gd.exclude = !show;
		layout();
	}
}
