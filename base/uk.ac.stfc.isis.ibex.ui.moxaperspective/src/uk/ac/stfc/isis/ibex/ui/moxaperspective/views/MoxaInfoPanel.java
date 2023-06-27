package uk.ac.stfc.isis.ibex.ui.moxaperspective.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class MoxaInfoPanel  extends Composite {
	private final Label beamCurrent;
	private final Label beamFrequency;
	private final Label beamEnergy;
	private final Label bunchLength;
	private final Label extractDelay;

	/**
	 * The constructor.
	 * 
	 * @param parent the parent
	 * @param style  the SWT style
	 */
	public MoxaInfoPanel(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(2, false));

		beamCurrent = createRow("Beam Current");
		beamFrequency = createRow("Beam Frequency");
		beamEnergy = createRow("Beam Energy");
		bunchLength = createRow("Bunch Length");
		extractDelay = createRow("Extract Delay");

	}

	private Label createRow(String name) {
		new Label(this, SWT.NONE).setText(name);

		Label display = new Label(this, SWT.BORDER | SWT.RIGHT);
		display.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		return display;
	}

	/**
	 * Binding observable facilityPV with the Label
	 * 
	 * @param ts
	 */
//	private void bind(SynchrotronObservables sync) {
//		bindAndAddMenu(sync.beamCurrent, beamCurrent, this);
//		bindAndAddMenu(sync.beamFrequency, beamFrequency, this);
//		bindAndAddMenu(sync.beamEnergy, beamEnergy, this);
//		bindAndAddMenu(sync.bunchLength, bunchLength, this);
//		bindAndAddMenu(sync.extractDelay, extractDelay, this);
//
//	}

}