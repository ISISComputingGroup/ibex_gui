package uk.ac.stfc.isis.ibex.ui.synoptic.widgets;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.jface.resource.JFaceResources;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.wb.swt.SWTResourceManager;

import uk.ac.stfc.isis.ibex.synoptic.Synoptic;
import uk.ac.stfc.isis.ibex.synoptic.SynopticInfo;
import uk.ac.stfc.isis.ibex.ui.synoptic.Activator;
import uk.ac.stfc.isis.ibex.ui.synoptic.SynopticPresenter;

/**
 * Contains the widget showing the synoptic drop down menu for switching, refresh button, and bread crumb trail.
 */
public class SynopticSelection extends Composite {
	
	private static final Color BACKGROUND = SWTResourceManager.getColor(240, 240, 240);
	
	// The synoptic drop down menu selector
	private Combo synopticCombo;
	
	private static Synoptic synoptic = Synoptic.getInstance();
	private final SynopticPresenter presenter = Activator.getDefault().presenter();
	private final Display display = Display.getCurrent();
	
	public SynopticSelection(Composite parent, int style) {
		super(parent, style);
		GridLayout gridLayout = new GridLayout(3, false);
		gridLayout.marginHeight = 2;
		gridLayout.marginRight = 5;
		gridLayout.marginLeft = 5;
		gridLayout.marginWidth = 2;

		setLayout(gridLayout);
		
		Label synopticLabel = new Label(this, SWT.NONE);
		GridData gd_gotoLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		gd_gotoLabel.verticalAlignment = SWT.CENTER;
		synopticLabel.setLayoutData(gd_gotoLabel);
		synopticLabel.setText("Synoptic");
		synopticLabel.setFont(JFaceResources.getFontRegistry().getBold(""));
		
		GridData gd_synopticCombo = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		gd_synopticCombo.widthHint = 120;
		synopticCombo = new Combo(this, SWT.READ_ONLY);
		synopticCombo.setLayoutData(gd_synopticCombo);
		
		presenter.addPropertyChangeListener("components", new PropertyChangeListener() {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
				display.asyncExec(new Runnable() {
					@Override
					public void run() {
						setSynopticList();
					}
				});
			}
		});
		
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
		
		Button refreshButton = new Button(this, SWT.NONE);
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
