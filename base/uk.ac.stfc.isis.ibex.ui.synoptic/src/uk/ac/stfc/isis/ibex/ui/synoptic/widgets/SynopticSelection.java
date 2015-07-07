package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
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
		
		synopticCombo.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				updateSynopticList();
			}

			// Do nothing
			@Override
			public void keyReleased(KeyEvent arg0) {}
			
		});
		
		
		synopticCombo.addMouseListener(new MouseListener() {
			// Do nothing for these methods
			@Override
			public void mouseDoubleClick(MouseEvent arg0) {}
			@Override
			public void mouseDown(MouseEvent arg0) {

			}

			@Override
			public void mouseUp(MouseEvent arg0) {
				updateSynopticList();
			}
		});
		
		setSynopticList();
				
		synopticCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String item = synopticCombo.getText();
				synoptic.setViewerSynoptic(item);
			}
		});
		
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
		
		InstrumentBreadCrumb instrumentTrail = new InstrumentBreadCrumb(parent, SWT.NONE);
		instrumentTrail.setLayoutData(new GridData(SWT.CENTER, SWT.FILL, false, false, 1, 1));
		instrumentTrail.setBackground(BACKGROUND);
	}
	
	public void setSynopticList() {
		String[] synoptics = synoptic.availableSynopticNames().toArray(new String[0]);
		synopticCombo.setItems(synoptics);
		
		// Show selected synoptic correctly on startup
		int selectedSynopticNumber = synoptic.getSynopticNumber();
		if (selectedSynopticNumber >= 0) {
			synopticCombo.select(selectedSynopticNumber);
		}		
	}
	
	public void updateSynopticList() {
		String[] synoptics = synoptic.availableSynopticNames().toArray(new String[0]);
		ArrayList<String> synopticsArrayList = new ArrayList<String>(Arrays.asList(synoptics)); 
		String[] synopticComboItems = synopticCombo.getItems();
		ArrayList<String> synopticComboItemsArrayList = new ArrayList<String>(Arrays.asList(synopticComboItems)); 
		
		// First add snyoptics not in list
		for (String synoptic : synoptics) {
			if (!synopticComboItemsArrayList.contains(synoptic)) {
				synopticCombo.add(synoptic);
			}
		}
		
		// Then remove synoptics that have gone
		for (String synoptic : synopticComboItems) {
			if (!synopticsArrayList.contains(synoptic)) {
				synopticCombo.remove(synoptic);
			}
		}
	}

}
