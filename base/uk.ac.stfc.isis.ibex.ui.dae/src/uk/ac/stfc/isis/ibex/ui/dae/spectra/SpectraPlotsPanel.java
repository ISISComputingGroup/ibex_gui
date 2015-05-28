package uk.ac.stfc.isis.ibex.ui.dae.spectra;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

import uk.ac.stfc.isis.ibex.dae.spectra.UpdatableSpectrum;

public class SpectraPlotsPanel extends Composite {

	private SpectrumView plot1;
	private SpectrumView plot2;
	private SpectrumView plot3;
	private SpectrumView plot4;
	private Composite plots;
	
	public SpectraPlotsPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));
		
		plots = new Composite(this, SWT.NONE);
		plots.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plots.setLayout(new GridLayout(2, false));
		
		plot1 = new SpectrumView(plots, SWT.NONE);
		plot1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plot1.setSize(398, 258);
		
		plot2 = new SpectrumView(plots, SWT.NONE);
		plot2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plot2.setSize(399, 258);
		
		plot3 = new SpectrumView(plots, SWT.NONE);
		plot3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plot3.setSize(398, 258);
		
		plot4 = new SpectrumView(plots, SWT.NONE);
		plot4.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		plot4.setSize(399, 258);
	}
	
	public void setModel(List<? extends UpdatableSpectrum> spectra) {
		plot1.setModel(spectra.get(0));
		plot2.setModel(spectra.get(1));
		plot3.setModel(spectra.get(2));
		plot4.setModel(spectra.get(3));
	}
}
