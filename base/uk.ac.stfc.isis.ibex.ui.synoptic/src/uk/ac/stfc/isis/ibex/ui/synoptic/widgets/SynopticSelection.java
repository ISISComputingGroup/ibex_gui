package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;

public class SynopticSelection extends Composite {
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	
	private Combo synopticCombo;
	
	private static Synoptic synoptic = Synoptic.getInstance();

	public SynopticSelection(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(2, false);
		gridLayout.verticalSpacing = 0;
		gridLayout.marginWidth = 0;
		gridLayout.marginHeight = 0;
		setLayout(gridLayout);
		
		Label synopticLabel = new Label(this, SWT.NONE);
		GridData gd_gotoLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_gotoLabel.verticalAlignment = SWT.CENTER;
		synopticLabel.setLayoutData(gd_gotoLabel);
		synopticLabel.setText("Synoptic:");
		
		// The synoptic drop down menu selector
		synopticCombo = new Combo(parent, SWT.READ_ONLY);
		GridData gd_synopticCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_synopticCombo.widthHint = 120;
		synopticCombo.setLayoutData(gd_synopticCombo);
		
		String[] synoptics = synoptic.availableSynopticNames().toArray(new String[0]);
		
		synopticCombo.setItems(synoptics);
		
		synopticCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String item = synopticCombo.getText();
				synoptic.setViewerSynoptic(item);
			}
		});
		
		// Show selected synoptic correctly on startup
		int selectedSynopticNumber = synoptic.getSynopticNumber();
		if (selectedSynopticNumber >= 0) {
			synopticCombo.select(selectedSynopticNumber);
		}
		
		Button refreshButton = new Button(parent, SWT.NONE);
		refreshButton.setText("Refresh Synoptic");
		refreshButton.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		refreshButton.setBackground(BACKGROUND);
		
		refreshButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SynopticInfo synopticToRefresh = synoptic.getSynopticInfo();
				synoptic.setViewerSynoptic(synopticToRefresh);		
			}
		});
	}

}
