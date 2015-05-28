package uk.ac.stfc.isis.ibex.ui.beamstatus.views;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.beamstatus.BeamStatus;
import uk.ac.stfc.isis.ibex.beamstatus.Observables.Synchrotron;

public class SynchrotronPanel extends Composite {

	private final Label beamCurrent;
	private final Label beamFrequency;
	
	public SynchrotronPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));
		
		Label lblBeamCurrent = new Label(this, SWT.NONE);
		lblBeamCurrent.setText("Beam Current");
		lblBeamCurrent.setAlignment(SWT.RIGHT);
		
		beamCurrent = new Label(this, SWT.BORDER | SWT.RIGHT);
		beamCurrent.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Label lblBeamFrequency = new Label(this, SWT.NONE);
		lblBeamFrequency.setText("Beam Frequency");
		
		beamFrequency = new Label(this, SWT.BORDER | SWT.RIGHT);
		beamFrequency.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		if (BeamStatus.getInstance() != null) {
			bind(BeamStatus.getInstance().observables().sync);
		}
	}
	
	private void bind(Synchrotron sync) {
		
		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(WidgetProperties.text().observe(beamCurrent), BeanProperties.value("value").observe(sync.beamCurrent));
		bindingContext.bindValue(WidgetProperties.text().observe(beamFrequency), BeanProperties.value("value").observe(sync.beamFrequency));
	}

}
